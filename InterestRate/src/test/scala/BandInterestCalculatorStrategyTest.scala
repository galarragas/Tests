package com.pragmasoft.tests.interestcalculator


import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class BandInterestCalculatorStrategyTest extends FlatSpec with ShouldMatchers {
  val strategy: BandInterestCalculatorStrategy = new BandInterestCalculatorStrategy(
    List((BigDecimal(0), BigDecimal(0.03)), (BigDecimal(100), BigDecimal(0.04)), (BigDecimal(200), BigDecimal(0.05)))
  )

  "A BandInterestCalculatorStrategy" should "apply the rate in the band with the greater lower bound less than the amount specified" in {
    strategy.getYearlyInterest(BigDecimal(150)) should equal(BigDecimal(150)*BigDecimal(0.04))
  }

  it should "apply the last band if the value is greater than any band boundary specified" in {
    strategy.getYearlyInterest(BigDecimal(250)) should equal(BigDecimal(250)*BigDecimal(0.05))
  }

  it should "consider the lower boundary inclusive" in {
    strategy.getYearlyInterest(BigDecimal(100)) should equal(BigDecimal(100)*BigDecimal(0.04))
  }

  it should "consider the first boundary for zero deposit value" in {
    strategy.getYearlyInterest(BigDecimal(1)) should equal(BigDecimal(1)*BigDecimal(0.03))
  }

  it should "require the first boundary to start from zero" in {
    evaluating {
      new BandInterestCalculatorStrategy(List((BigDecimal(1), BigDecimal(0.01)), (BigDecimal(2), BigDecimal(0.01))))
    } should produce [IllegalArgumentException]
  }

  it should "require at least one boundary" in {
    evaluating {
      new BandInterestCalculatorStrategy(List.empty)
    } should produce [IllegalArgumentException]
  }
}
