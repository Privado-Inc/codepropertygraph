package io.shiftleft.passes

import io.shiftleft.SerializedCpg
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.utils.ExecutionContextProvider
import org.slf4j.MDC

import java.util.concurrent.LinkedBlockingQueue
import scala.annotation.nowarn
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

abstract class ConcurrentQueueWriterCpgPass[T <: AnyRef](
  cpg: Cpg,
  @nowarn outName: String = "",
  keyPool: Option[KeyPool] = None
) extends NewStyleCpgPassBase[T] {

  @volatile var nDiffT = -1

  /** WARNING: runOnPart is executed in parallel to committing of graph modifications. The upshot is that it is unsafe
    * to read ANY data from cpg, on pain of bad race conditions
    *
    * Only use ConcurrentWriterCpgPass if you are _very_ sure that you avoid races.
    *
    * E.g. adding a CFG edge to node X races with reading an AST edge of node X.
    */
  override def createApplySerializeAndStore(
    serializedCpg: SerializedCpg,
    inverse: Boolean = false,
    prefix: String = ""
  ): Unit = {
    baseLogger.info(s"Start of enhancement: $name")
    val nanosStart = System.nanoTime()
    var nParts     = 0
    var nDiff      = 0
    nDiffT = -1
    init()
    val parts = generateParts()
    nParts = parts.size
    val writer       = new Writer(MDC.getCopyOfContextMap())
    val writerThread = new Thread(writer)
    writerThread.setName("Writer")
    writerThread.start()
    implicit val ec: ExecutionContext = ExecutionContextProvider.getExecutionContext
    try {
      try {
        val futures = parts
          .map(part => {
            Future {
              val builder = new DiffGraphBuilder
              runOnPart(builder, part.asInstanceOf[T])
              writer.queue.put(Some(builder.build()))
            }
          })
          .toList
        val allResults: Future[List[Unit]] = Future.sequence(futures)
        Await.result(allResults, Duration.Inf)
        if (writer.raisedException != null)
          throw writer.raisedException // will be wrapped with good stacktrace in the finally block
      } finally {
        try {
          // if the writer died on us, then the queue might be full and we could deadlock
          if (writer.raisedException == null) writer.queue.put(None)
          writerThread.join()
          // we need to reraise exceptions
          if (writer.raisedException != null)
            throw new RuntimeException("Failure in diffgraph application", writer.raisedException)

        } finally { finish() }
      }
    } finally {
      // the nested finally is somewhat ugly -- but we promised to clean up with finish(), we want to include finish()
      // in the reported timings, and we must have our final log message if finish() throws

      val nanosStop = System.nanoTime()

      baseLogger.info(
        f"Enhancement $name completed in ${(nanosStop - nanosStart) * 1e-6}%.0f ms. ${nDiff}%d  + ${nDiffT - nDiff}%d changes committed from ${nParts}%d parts."
      )
    }
  }

  private class Writer(mdc: java.util.Map[String, String]) extends Runnable {

    val queue =
      new LinkedBlockingQueue[Option[overflowdb.BatchedUpdate.DiffGraph]]()

    @volatile var raisedException: Exception = null

    override def run(): Unit = {
      try {
        nDiffT = 0
        // logback chokes on null context maps
        if (mdc != null) MDC.setContextMap(mdc)
        var terminate  = false
        var index: Int = 0
        while (!terminate) {
          queue.take() match {
            case None =>
              baseLogger.debug("Shutting down WriterThread")
              terminate = true
            case Some(diffGraph) =>
              nDiffT += overflowdb.BatchedUpdate
                .applyDiff(cpg.graph, diffGraph, keyPool.getOrElse(null), null)
                .transitiveModifications()
              index += 1
          }
        }
      } catch {
        case exception: InterruptedException => baseLogger.warn("Interrupted WriterThread", exception)
        case exc: Exception =>
          raisedException = exc
          queue.clear()
          throw exc
      }
    }
  }

}
