import org.scalatest._

package com.pragmasoft.puzzles.numberconverter.token {

import matchers.ShouldMatchers
import org.scalamock.scalatest.MockFactory
import org.scalamock.{MockFactoryBase}

class UnitTokenSpec extends FlatSpec with ShouldMatchers {

  "A UnitToken" should "throw exception if value is zero" in {
    evaluating {
      new UnitToken(0)
    } should produce[IllegalArgumentException]
  }

  it should "throw exception if value is greater than nine" in {
    evaluating {
      new UnitToken(10)
    } should produce[IllegalArgumentException]
  }

  it should "convert values between 1 and 9" in {
    (new UnitToken(1)).asString(false) should equal("one")
    (new UnitToken(2)).asString(false) should equal("two")
  }

  it should "have simple padding" in {
    (new UnitToken(1)).asString(true) should equal(" one")
  }
}


class IrregularUnderTwentyTokenSpec extends FlatSpec with ShouldMatchers {
  "An Irregular Under Twenty Token " should "throw exception if value is greater than nineteen" in {
    evaluating {new IrregularUnderTwentyToken(20) } should produce[IllegalArgumentException]
  }

  it should "convert zero" in {new IrregularUnderTwentyToken(0).asString(false) should equal("zero")}

  it should "convert values from 1 to 19" in {
    new IrregularUnderTwentyToken(1).asString(false) should equal("one")
    new IrregularUnderTwentyToken(2).asString(false) should equal("two")
  }

  it should "have and padding" in {
    (new IrregularUnderTwentyToken(1)).asString(true) should equal(" and one")
  }
}

class DelegateTokenSuite extends FunSuite with MockFactory {

  test("Should call delegate no prefix") {
    val innerToken = mock[NumberToken]

    val delegateToken = new ThousandthToken(innerToken)

    (innerToken.asString _) expects false

    delegateToken.asString(false)
  }

  test("Should call delegate with prefix") {
    val innerToken = stub[NumberToken]

    val delegateToken = new ThousandthToken(innerToken)

    delegateToken.asString(true)

    innerToken.asString _  verify true
  }

  test("Should ask to delegate if is empty") {
    val innerToken = stub[NumberToken]

    val delegateToken = new ThousandthToken(innerToken)

    (innerToken.isEmpty _).when().returns(true)

    assert(delegateToken.isEmpty)
    innerToken.isEmpty _ verify
  }
}

}
