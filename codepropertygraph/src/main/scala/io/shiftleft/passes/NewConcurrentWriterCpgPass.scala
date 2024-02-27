package io.shiftleft.passes

import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.utils.ConcurrentProcessor
import org.slf4j.LoggerFactory
import overflowdb.BatchedUpdate.DiffGraphBuilder

import scala.annotation.nowarn

abstract class NewConcurrentWriterCpgPass[T <: AnyRef](
  cpg: Cpg,
  @nowarn outName: String = "",
  keyPool: Option[KeyPool] = None
) extends ConcurrentProcessor[T, Unit](()) {
  private val logger           = LoggerFactory.getLogger(this.getClass)
  private val MERGE_DIFF_GRAPH = "MERGE_DIFF_GRAPH"

  def runOnPartForCpg(diffGraphBuilder: overflowdb.BatchedUpdate.DiffGraphBuilder, part: T): Unit

  override def runOnPart(part: T): Unit = {
    val builder = new DiffGraphBuilder
    runOnPartForCpg(builder, part)
    addInWriterQueue(MERGE_DIFF_GRAPH, builder.build())
  }
  override def processCommand(command: String, item: Any, result: Unit): Unit = {
    try {
      command match {
        case MERGE_DIFF_GRAPH =>
          val diffGraph = item.asInstanceOf[overflowdb.BatchedUpdate.DiffGraph]
          overflowdb.BatchedUpdate
            .applyDiff(cpg.graph, diffGraph, keyPool.getOrElse(null), null)
            .transitiveModifications()
      }
    } catch
    case ex: Exception =>
      logger.info(s"Error while processing NewConcurrentWriterCpgPass Writer command '${command}' - item - ${item}", ex)
  }

}
