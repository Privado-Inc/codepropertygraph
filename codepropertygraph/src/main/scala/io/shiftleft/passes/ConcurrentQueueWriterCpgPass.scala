package io.shiftleft.passes

import io.shiftleft.SerializedCpg
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.utils.ConcurrentTaskUtil
import org.slf4j.LoggerFactory
import overflowdb.BatchedUpdate.DiffGraphBuilder

import java.util.concurrent.Callable
import scala.annotation.nowarn
import scala.util.Failure

abstract class ConcurrentQueueWriterCpgPass[T <: AnyRef](
  cpg: Cpg,
  @nowarn outName: String = "",
  keyPool: Option[KeyPool] = None
) extends NewStyleCpgPassBase[T]
    with BaseConcurrentQueueProcessor[T, Cpg] {
  private val MERGE_DIFF_GRAPH = "MERGE_DIFF_GRAPH"

  override def createApplySerializeAndStore(
    serializedCpg: SerializedCpg,
    inverse: Boolean = false,
    prefix: String = ""
  ): Unit = {
    baseLogger.info(s"Start of enhancement: $name")
    init()
    val parts = generateParts()
    Option(parts) match {
      case Some(parts) if parts.size > 0 =>
        val queueWriter = new Thread(Writer(cpg))
        queueWriter.start()
        ConcurrentTaskUtil
          .runUsingThreadPool(parts.map(part => () => new TaskProcessor(part.asInstanceOf[T]).call()).iterator)
          .map {
            case Failure(e) =>
              logger.error("Exception encountered inside TaskProcessor", e)
            case _ =>
          }
        pushStopCommandInQueue()
        queueWriter.join()
      case _ =>
        baseLogger.debug(s"parts array is empty while running pass $name")
    }
    finish()
  }

  private class TaskProcessor(part: T) extends Callable[Unit] {
    override def call(): Unit = {
      val builder = new DiffGraphBuilder
      runOnPart(builder, part)
      addInWriterQueue(MERGE_DIFF_GRAPH, builder.build())
      cleanup()
    }
  }

  override def processCommand(command: String, item: Any, result: Cpg): Unit = {
    try {
      command match {
        case MERGE_DIFF_GRAPH =>
          val diffGraph = item.asInstanceOf[overflowdb.BatchedUpdate.DiffGraph]
          overflowdb.BatchedUpdate
            .applyDiff(result.graph, diffGraph, keyPool.getOrElse(null), null)
            .transitiveModifications()
      }
    } catch
    case ex: Exception =>
      logger.info(
        s"Error while processing ConcurrentQueueWriterCpgPass Writer command '${command}' - item - ${item}",
        ex
      )
  }

}
