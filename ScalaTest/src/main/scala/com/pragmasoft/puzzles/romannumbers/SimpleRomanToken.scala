package com.pragmasoft.puzzles.romannumbers

trait RomanTokenGenerator {
  def toRoman(value: Int): (String, Int)
}

trait RomanTokenParser {
  def parse(toParse: String, startFrom: Int = 0): (Int, Int)
}

class SimpleRomanToken(val tokenStringRepresentation: String, val tokenValue: Int, val maxRepetitions: Int = 1) extends RomanTokenParser with RomanTokenGenerator {
  override def toRoman(value: Int): (String, Int) = {
    val quotient = (value / tokenValue)
    if (quotient <= maxRepetitions) {
      (tokenStringRepresentation * quotient, value % tokenValue)
    } else {
      (tokenStringRepresentation * maxRepetitions, value - (maxRepetitions * tokenValue))
    }
  }

  /**
   * @param toParse
   * @param startFrom
   * @return the value of the parsed part of the string and the index the string has been parsed to
   */
  override def parse(toParse: String, startFrom: Int = 0): (Int, Int) = {
    def parseWithMatchedOccurrenciesCount(currIndex: Int, matchedOccurrencies: Int = 0): (Int, Int) = {
      if ((matchedOccurrencies >= maxRepetitions) || !toParse.startsWith(tokenStringRepresentation, currIndex)) (0, currIndex)
      else {
        val (furtherParsedVal, finalIndex) = parseWithMatchedOccurrenciesCount(currIndex + tokenStringRepresentation.size, matchedOccurrencies + 1)
        (tokenValue + furtherParsedVal, finalIndex)
      }
    }
    parseWithMatchedOccurrenciesCount(startFrom)
  }
}


class CompositeAlternativeTokenParser(val highestPriorityToken: RomanTokenParser, val lowestPriorityToken: RomanTokenParser) extends RomanTokenParser {
  def parse(toParse: String, startFrom: Int): (Int, Int) = {
    val (firstResult, firstNewIndex) = highestPriorityToken.parse(toParse, startFrom)

    if (firstResult > 0) return (firstResult, firstNewIndex)
    else lowestPriorityToken.parse(toParse, startFrom)
  }
}

class ChainRomanTokenParser(val delegates: List[RomanTokenParser]) extends RomanTokenParser {

  class TokeniserAccumulator(val value: Int, val offset: Int)

  implicit def accumulatorToTuple(accumulator: TokeniserAccumulator): (Int, Int) = (accumulator.value, accumulator.offset)

  def parse(toParse: String, startFrom: Int): (Int, Int) =
    delegates.foldLeft(new TokeniserAccumulator(0, startFrom))((currAccumulator: TokeniserAccumulator, currToken: RomanTokenParser) => {
      val (curParsedVal, currEndOffset) = currToken.parse(toParse, currAccumulator.offset)
      new TokeniserAccumulator(curParsedVal + currAccumulator.value, currEndOffset)
    }
    )
}

class ChainRomanTokenGenerator(val delegates: List[RomanTokenGenerator]) extends RomanTokenGenerator {

  private class ConversionAccumulator(val convertedString: String, val remainder: Int)

  implicit def accumulatorToTuple(accumulator: ConversionAccumulator): (String, Int) = (accumulator.convertedString, accumulator.remainder)

  def toRoman(value: Int): (String, Int) = {
    require(value > 0)

    delegates.foldLeft(new ConversionAccumulator("", value))((currAccumulator: ConversionAccumulator, currGenerator: RomanTokenGenerator) => {
      val (tokenConversion, remainder) = currGenerator.toRoman(currAccumulator.remainder)
      new ConversionAccumulator(currAccumulator.convertedString + tokenConversion, remainder)
    }
    )
  } ensuring (_.remainder == 0)
}