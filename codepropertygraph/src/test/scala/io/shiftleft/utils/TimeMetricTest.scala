package io.shiftleft.utils

import better.files.File
import io.shiftleft.codepropertygraph.Cpg
import io.shiftleft.codepropertygraph.generated.nodes.NewFile
import io.shiftleft.passes.{ConcurrentWriterCpgPass, KeyPool, NewStyleCpgPassBase}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.Calendar
class TimeMetricTest extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  private object Fixture {
    def apply(
      keyPools: Option[Iterator[KeyPool]] = None
    )(f: (Cpg, List[NewStyleCpgPassBase[String]], File, TimeMetric) => Unit): Unit = {
      File.usingTemporaryFile("performance", ".csv") { file =>
        file.delete()
        val filename = file.path.toString
        val timeMetric =
          TimeMetric(timeMetricRecordConfig = Some(TimeMetricRecordConfig(resultFile = filename, recordFreq = 100)))

        val cpg  = Cpg.emptyCpg
        val pool = keyPools.flatMap(_.nextOption())
        class MyPassOne(cpg: Cpg, timeMetric: TimeMetric)
            extends ConcurrentWriterCpgPass[String](cpg, "MyPassOne", pool, Option(timeMetric)) {
          override def generateParts(): Array[String] = Array("foo", "bar")

          override def runOnPart(diffGraph: DiffGraphBuilder, part: String): Unit = {
            diffGraph.addNode(NewFile().name(part))
          }
        }
        class MyPassTwo(cpg: Cpg, timeMetric: TimeMetric)
            extends ConcurrentWriterCpgPass[String](cpg, "MyPassTwo", pool, Option(timeMetric)) {
          override def generateParts(): Array[String] = Array("footwo", "bartwo")

          override def runOnPart(diffGraph: DiffGraphBuilder, part: String): Unit = {
            diffGraph.addNode(NewFile().name(part))
          }
        }
        val passes = List(new MyPassOne(cpg, timeMetric), new MyPassTwo(cpg, timeMetric))
        f(cpg, passes, file, timeMetric)
      }
    }
  }

  "Time Metric Stats collection test" should {

    "Simple test" in Fixture() { (_, passes, file, timeMetric) =>
      timeMetric.initiateNewStage("Group Stage")
      passes.foreach(_.createAndApply())
      timeMetric.endLastStage()
      timeMetric.endTheTotalProcessing("Done all")
      file.exists shouldBe true
      file.lineIterator.foreach(println)
      file.lineIterator.exists(_.contains("Group Stage, <not set>, Started")) shouldBe true
      file.lineIterator.exists(_.contains("Group Stage, MyPassOne, Started")) shouldBe true
      file.lineIterator.exists(_.contains("Group Stage, MyPassOne, Done")) shouldBe true
      file.lineIterator.exists(_.contains("Group Stage, MyPassTwo, Started")) shouldBe true
      file.lineIterator.exists(_.contains("Group Stage, MyPassTwo, Done")) shouldBe true
      file.lineIterator.exists(_.contains("Group Stage, <not set>, Done")) shouldBe true
      file.lineIterator.exists(_.contains("Done all, Total Time taken")) shouldBe true
      timeMetric.stagePerformance.size shouldBe 4
    }
  }

  "Millisecond String format " should {
    "Millisecond" in {
      val timeMetric = TimeMetric()
      val firstTime  = timeMetric.cal.getTime
      timeMetric.cal.add(Calendar.MILLISECOND, 10)
      val newTime = timeMetric.cal.getTime
      val str     = timeMetric.getDiffFormatted(newTime.getTime - firstTime.getTime)
      str shouldBe "10 ms - 00h:00m:00s:10ms"

    }
    "Hours, Min, Second and Milliseconds" in {
      val timeMetric = TimeMetric()
      val firstTime  = timeMetric.cal.getTime
      timeMetric.cal.add(Calendar.MILLISECOND, 10)
      timeMetric.cal.add(Calendar.SECOND, 11)
      timeMetric.cal.add(Calendar.MINUTE, 12)
      timeMetric.cal.add(Calendar.HOUR, 13)
      val newTime = timeMetric.cal.getTime
      val str     = timeMetric.getDiffFormatted(newTime.getTime - firstTime.getTime)
      str shouldBe "47531010 ms - 13h:12m:11s:10ms"
    }
  }
}
