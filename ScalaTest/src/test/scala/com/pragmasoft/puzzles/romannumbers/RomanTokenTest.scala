package com.pragmasoft.puzzles.romannumbers

import org.scalatest.{BeforeAndAfter, FunSuite, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalamock.scalatest.MockFactory
import com.pragmasoft.puzzles.numberconverter.token.{ThousandthToken, NumberToken}

class SingleCharRomanTokenRomanToIntTest extends FlatSpec with ShouldMatchers {

  "A single char parser" should "parse a string with itself" in {
    new SimpleRomanToken("I", 1, 3).parse("I") should equal((1,1))
  }

  it should "parse a string with many repetisions of itself" in {
    new SimpleRomanToken("I", 1, 4).parse("III") should equal((3,3))
  }

  it should "parse a string with many repetisions of itself until the maximum allowed repetions" in {
    new SimpleRomanToken("I", 1, 3).parse("IIIIIIII") should equal((3,3))
  }

  it should "skip any parsing if the string starts with an unrecognised char" in {
    new SimpleRomanToken("I", 1, 3).parse("CIII") should equal((0,0))
  }

  it should "skip any parsing for empty string" in {
    new SimpleRomanToken("I", 1, 3).parse("") should equal((0,0))
  }
}

class MultiCharRomanTokenRomanToIntTest extends FlatSpec with ShouldMatchers {

  "A multi char parser" should "parse a string with itself" in {
    new SimpleRomanToken("IV", 4, 3).parse("IV") should equal((4,2))
  }

  it should "parse a string with many repetisions of itself" in {
    new SimpleRomanToken("IX", 9, 3).parse("IXIX") should equal((18,4))
  }

  it should "parse a string with many repetisions of itself until the maximum allowed repetions" in {
    new SimpleRomanToken("XL", 40, 1).parse("XLXLXL") should equal((40,2))
  }

  it should "skip any parsing if the string starts with an unrecognised sequence" in {
    new SimpleRomanToken("IV", 4, 3).parse("CL") should equal((0,0))
  }

  it should "skip any parsing if the string starts with an partially matching sequence" in {
    new SimpleRomanToken("IV", 4, 1).parse("IX") should equal((0,0))
  }

  it should "skip any parsing for empty string" in {
    new SimpleRomanToken("IV", 4, 1).parse("") should equal((0,0))
  }
}

class RomanTokenIntToRomanTest extends FlatSpec with ShouldMatchers {
  "A roman token" should "convert its own exact value from integer to roman" in {
    new SimpleRomanToken("IX", 9, 1).toRoman(9) should equal(("IX", 0))
  }

  it should "convert a multiple of its value" in {
    new SimpleRomanToken("I", 1, 3).toRoman(3) should equal(("III",0))
  }

  it should "return the remainder" in {
    new SimpleRomanToken("X", 10, 3).toRoman(39) should equal(("XXX", 9))
  }

  it should "return the full number if not greater than any multiple" in {
    new SimpleRomanToken("L", 50, 1).toRoman(13) should equal(("", 13))
  }

  it should "in any case return up to maxRepetitions of its token" in {
    new SimpleRomanToken("I", 1, 3).toRoman(5) should equal("III", 2)
  }
}

class CompositeAlternativeTokenParserTest extends FunSuite with MockFactory with BeforeAndAfter {
  test("Should try both parser in order if no match at first") {
    val hiPrioParser = stub[RomanTokenParser]
    val loPrioParser = stub[RomanTokenParser]
    val delegateToken = new CompositeAlternativeTokenParser(hiPrioParser, loPrioParser)

    //both parsers are not parsing any value
    (hiPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))
    (loPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))

    delegateToken.parse("IXX", 3)

    inSequence {
      (hiPrioParser.parse _).verify("IXX", 3)
      (loPrioParser.parse _).verify("IXX", 3)
    }
  }

  test("Should return zero and origin index if both parsers are failing") {
    val hiPrioParser = stub[RomanTokenParser]
    val loPrioParser = stub[RomanTokenParser]
    val delegateToken = new CompositeAlternativeTokenParser(hiPrioParser, loPrioParser)

    //both parsers are not parsing any value
    (hiPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))
    (loPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))

    assert(delegateToken.parse("IXX", 3) === (0,3))
  }

  test("Should not call second parser if first one parses a value") {
    val hiPrioParser = stub[RomanTokenParser]
    val loPrioParser = stub[RomanTokenParser]
    val delegateToken = new CompositeAlternativeTokenParser(hiPrioParser, loPrioParser)

    //both parsers are not parsing any value
    (hiPrioParser.parse _).when(*, *).onCall((toParse, from) => (1, 4))
    (loPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))

    assert(delegateToken.parse("IXX", 3) === (1,4))

    inSequence {
      (hiPrioParser.parse _).verify("IXX", 3)
      (loPrioParser.parse _).verify(*, *).never()
    }
  }

  test("Should call second parser if first one parses has doesn't parse") {
    val hiPrioParser = stub[RomanTokenParser]
    val loPrioParser = stub[RomanTokenParser]
    val delegateToken = new CompositeAlternativeTokenParser(hiPrioParser, loPrioParser)

    //both parsers are not parsing any value
    (hiPrioParser.parse _).when(*, *).onCall((toParse, from) => (0, from))
    (loPrioParser.parse _).when(*, *).onCall((toParse, from) => (1, 4))

    assert(delegateToken.parse("IXX", 3) === (1,4))

    inSequence {
      (hiPrioParser.parse _).verify("IXX", 3)
      (loPrioParser.parse _).verify("IXX", 3)
    }
  }
}

class ChainRomanTokenParserTest extends FunSuite with MockFactory {
  test("Should call all parsers in sequence chaining result") {
    val parser1 = stub[RomanTokenParser]
    val parser2 = stub[RomanTokenParser]
    val parser3 = stub[RomanTokenParser]
    val parser4 = stub[RomanTokenParser]

    (parser1.parse _).when(*, *).onCall((toParse, from) => (1, from+1))
    (parser2.parse _).when(*, *).onCall((toParse, from) => (2, from+2))
    (parser3.parse _).when(*, *).onCall((toParse, from) => (3, from+3))
    (parser4.parse _).when(*, *).onCall((toParse, from) => (4, from+4))

    val chainParser = new ChainRomanTokenParser(parser1 :: parser2 :: parser3 :: parser4 :: List.empty)

    assert(chainParser.parse("String", 1) === (10, 11))

    inSequence {
      (parser1.parse _).verify("String", 1)
      (parser2.parse _).verify("String", 2)
      (parser3.parse _).verify("String", 4)
      (parser4.parse _).verify("String", 7)
    }
  }
}
