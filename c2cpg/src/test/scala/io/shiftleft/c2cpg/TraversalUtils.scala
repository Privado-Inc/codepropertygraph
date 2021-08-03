package io.shiftleft.c2cpg

import io.shiftleft.codepropertygraph.generated.{NodeTypes, Properties}
import org.scalatest.matchers.should.Matchers
import overflowdb._

trait TraversalUtils extends Matchers {

  protected val fixture: CpgTestFixture

  def getMethod(name: String): List[Node] = {
    val result =
      fixture.traversalSource
        .label(NodeTypes.METHOD)
        .has(Properties.NAME -> name)
        .l

    result.size shouldBe 1
    result
  }

}