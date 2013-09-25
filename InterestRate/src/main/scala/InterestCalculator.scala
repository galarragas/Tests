package com.pragmasoft.tests.interestcalculator

sealed trait InterestCalculatorStrategy {
  def getYearlyInterest(accountBalance : BigDecimal) : BigDecimal
}

package object Types {
  type InterestBand = (BigDecimal, BigDecimal)
}

class BandInterestCalculatorStrategy(bands : List[Types.InterestBand]) extends InterestCalculatorStrategy {
  require(bands.length > 0)
  require(bands.head._1 == BigDecimal(0))

  def getYearlyInterest(accountBalance: BigDecimal): BigDecimal = bandFor(accountBalance)._2 * accountBalance

  def bandFor(accountBalance: BigDecimal): Types.InterestBand = {
    bands.filter(interestBand => (interestBand._1 <= accountBalance)).reverse.head
  }
}