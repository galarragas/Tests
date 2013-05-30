package com.pragmasoft.puzzles.numberconverter.token {

abstract class NumberToken {
  def isEmpty: Boolean

  def asString(hasPrecedingTokens: Boolean): String
}

trait SimplePadding {
  def doPadding(hasPrecedingTokens: Boolean): String = if (hasPrecedingTokens) " " else ""
}

trait AndPadding {
  def doPadding(hasPrecedingTokens: Boolean): String = if (hasPrecedingTokens) " and " else ""
}

abstract class AbstractSimpleToken(value: Int) extends NumberToken {
  require(valueIndex >= 0, "Value " + value + " is less than minimum allowed")
  require(valueIndex < stringValues.length, "Value " + value + " is more than maximum allowed")

  def stringValues: Array[String]

  def valueOffset: Int

  private final def valueIndex = value - valueOffset

  protected def doPadding(hasPrecedingTokens: Boolean): String

  def asString(hasPrecedingTokens: Boolean): String = doPadding(hasPrecedingTokens) + stringValues(valueIndex)

  override final def isEmpty = false
}

class UnitToken(value: Int) extends AbstractSimpleToken(value) with SimplePadding {
  def stringValues = Array("one", "two", "three", "four", "five"
    , "six", "seven", "eight", "nine")

  def valueOffset = 1
}

class IrregularUnderTwentyToken(value: Int) extends AbstractSimpleToken(value) with AndPadding {
  def stringValues = Array("zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
    "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eigtheen", "nineteen");

  def valueOffset = 0
}

class TenthToken(value: Int) extends AbstractSimpleToken(value) with AndPadding {
  def stringValues = Array("twenty", "thirty", "forty", "fifty", "sixty",
    "seventy", "eighty", "ninety");

  def valueOffset = 2;
}

class HundrethToken(value: Int) extends AbstractSimpleToken(value) with SimplePadding {
  def stringValues = Array("one hundred", "two hundred", "three hundred", "four hundred", "five hundred"
    , "six hundred", "seven hundred", "eight hundred", "nine hundred");

  def valueOffset = 1;
}

class CompositeToken(innerTokens: Array[NumberToken]) extends NumberToken with SimplePadding {
  def isEmpty: Boolean = innerTokens.forall(token => token.isEmpty)

  def asString(hasPrecedingTokens: Boolean): String =
    innerTokens.foldLeft(("", hasPrecedingTokens))(
      (base: (String, Boolean), currToken: NumberToken) => ((base._1 + currToken.asString(base._2)), base._2 || !currToken.isEmpty))._1

}

abstract class DelegateToken(value: NumberToken) extends NumberToken with SimplePadding {
  override final def asString(hasPrecedingTokens: Boolean): String = value.asString(hasPrecedingTokens) + " " + suffix

  override final def isEmpty = value.isEmpty

  def suffix: String
}

class ThousandthToken(value: NumberToken) extends DelegateToken(value) {
  override def suffix = "thousand"
}

class MillionToken(value: NumberToken) extends DelegateToken(value) {
  override def suffix = "million"
}

}

object main {
  import com.pragmasoft.puzzles.numberconverter.token._

  def main(args: Array[String]) {
    println(new UnitToken(4).asString(true))
    println(new IrregularUnderTwentyToken(0).asString(true))
    println(new TenthToken(3).asString(false))
    println(new CompositeToken(Array(new HundrethToken(4), new TenthToken(3), new UnitToken(2))).asString(false))
    println(new CompositeToken(Array(new HundrethToken(4), new TenthToken(3), new UnitToken(2))).asString(true))
    println(new MillionToken(new CompositeToken(Array(new HundrethToken(4), new TenthToken(3), new UnitToken(2)))).asString(false))
    println(new CompositeToken(Array(new HundrethToken(4), new TenthToken(3), new UnitToken(2))).asString(true))
    println(new ThousandthToken(new UnitToken(2)).asString(true))
  }
}
