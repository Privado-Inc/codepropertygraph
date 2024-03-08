package io.shiftleft.passes

import io.shiftleft.passes.GenericConcurrentQueueProcessor
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ListBuffer

class SampleConcurrentProcessor(result: ListBuffer[String])
    extends GenericConcurrentQueueProcessor[String, ListBuffer[String]](result) {
  override def generateParts(): Array[String] = Array("first", "second", "third", "forth")

  override def runOnPart(part: String): Unit = {
    addInWriterQueue("ADD", s"${part}-message")
  }

  override def processCommand(command: String, item: Any, result: ListBuffer[String]): Unit = {
    command match {
      case "ADD" => result += item.asInstanceOf[String]
      case _     => println("This command is not handled")
    }
  }
}

class GenericConcurrentQueueProcessorTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  "Concurrent processor sample" in {
    val results = new SampleConcurrentProcessor(ListBuffer.empty[String]).process()
    results.contains("first-message") shouldBe true
    results.contains("second-message") shouldBe true
    results.contains("third-message") shouldBe true
    results.contains("forth-message") shouldBe true
  }
}
