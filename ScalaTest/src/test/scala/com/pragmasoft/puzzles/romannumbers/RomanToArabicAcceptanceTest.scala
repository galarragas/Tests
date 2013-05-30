package com.pragmasoft.puzzles.romannumbers

import org.scalatest._

class RomanToIntAcceptanceTest extends FlatSpec with ShouldMatchers {
  val romanToArabic = new RomanConverter

  "A RomanConverter Converter" should "convert simple roman numbers" in {
    romanToArabic.fromRoman("I")  should equal((1, ""))
    romanToArabic.fromRoman("V")  should equal((5, ""))
    romanToArabic.fromRoman("X")  should equal((10, ""))
    romanToArabic.fromRoman("L")  should equal((50, ""))
    romanToArabic.fromRoman("C")  should equal((100, ""))
  }

  it should "convert valid single type composed roman numbers" in {
    romanToArabic.fromRoman("II") should equal((2, ""))
    romanToArabic.fromRoman("III") should equal((3, ""))
    romanToArabic.fromRoman("XX") should equal((20, ""))
    romanToArabic.fromRoman("XXX")  should equal((30, ""))
    romanToArabic.fromRoman("CC") should equal((200, ""))
  }

  it should "convert valid roman special numbers" in {
    romanToArabic.fromRoman("IV") should equal((4, ""))
    romanToArabic.fromRoman("IX") should equal((9, ""))
    romanToArabic.fromRoman("XL")  should equal((40, ""))
    romanToArabic.fromRoman("XC") should equal((90, ""))
  }

  it should "convert valid multi token composed numbers" in {
    romanToArabic.fromRoman("XXXIV") should equal((34, ""))
    romanToArabic.fromRoman("LIX") should equal((59, ""))
    romanToArabic.fromRoman("CXLIX") should equal((149, ""))
    romanToArabic.fromRoman("CCCV") should equal((305, ""))
    romanToArabic.fromRoman("CCLXXVII") should equal((277, ""))
  }

  it should "return unparsed part" in {
    romanToArabic.fromRoman("IIICLV") should equal((3, "CLV"))
    romanToArabic.fromRoman("XXIVABCD") should equal((24, "ABCD"))
  }

  it should "parse ix after 'X's" in {
    romanToArabic.fromRoman("XXIX") should equal((29, ""))
  }

  it should "not parse units after iv token" in {
    romanToArabic.fromRoman("XXIVIII") should equal((24, "III"))
    romanToArabic.fromRoman("XXIVV") should equal((24, "V"))
  }

  it should "not parse tenths or units after ix token" in {
    romanToArabic.fromRoman("LIXXX") should equal((59, "XX"))
    romanToArabic.fromRoman("LIXII") should equal((59, "II"))
    romanToArabic.fromRoman("LIXVII") should equal((59, "VII"))
    romanToArabic.fromRoman("LIXXVII") should equal((59, "XVII"))
  }

  it should "not parse hundredths or tenths after XC token" in {
    romanToArabic.fromRoman("XCCC") should equal((90, "CC"))
    romanToArabic.fromRoman("XCXX") should equal((90, "XX"))
    romanToArabic.fromRoman("XCLXXIV") should equal((90, "LXXIV"))
  }

  it should "parse units after XC token" in {
    romanToArabic.fromRoman("XCIX") should equal((99, ""))
    romanToArabic.fromRoman("XCIV") should equal((94, ""))
    romanToArabic.fromRoman("XCVIII") should equal((98, ""))
  }

  it should "return zero and all the string if starting with unrecognised chars" in {
    romanToArabic.fromRoman("Unrecognized chards LIV") should equal((0, "Unrecognized chards LIV"))
  }
}


class IntToRomanAcceptanceTest extends FlatSpec with ShouldMatchers {
  val romanToArabic = new RomanConverter

  "A RomanConverter" should "convert an int to its roman symbol" in {
    romanToArabic.toRoman(1) should equal("I")
    romanToArabic.toRoman(5) should equal("V")
    romanToArabic.toRoman(10) should equal("X")
    romanToArabic.toRoman(50) should equal("L")
    romanToArabic.toRoman(100) should equal("C")
  }

  it should "convert an int to the special composite symbol" in {
    romanToArabic.toRoman(4) should equal("IV")
    romanToArabic.toRoman(9) should equal("IX")
    romanToArabic.toRoman(40) should equal("XL")
    romanToArabic.toRoman(90) should equal("XC")
  }

  it should "convert an exact multiple of a basic symbol" in {
    romanToArabic.toRoman(2) should equal("II")
    romanToArabic.toRoman(30) should equal("XXX")
    romanToArabic.toRoman(300) should equal("CCC")
  }

  it should "convert complex numbers" in {
    romanToArabic.toRoman(6) should equal("VI")
    romanToArabic.toRoman(8) should equal("VIII")
    romanToArabic.toRoman(14) should equal("XIV")
    romanToArabic.toRoman(39) should equal("XXXIX")
    romanToArabic.toRoman(48) should equal("XLVIII")
    romanToArabic.toRoman(84) should equal("LXXXIV")
    romanToArabic.toRoman(97) should equal("XCVII")
    romanToArabic.toRoman(378) should equal("CCCLXXVIII")
    romanToArabic.toRoman(349) should equal("CCCXLIX")
  }

  it should "consider zero an invalid argument" in {
    evaluating {romanToArabic.toRoman(0) } should produce [IllegalArgumentException]
  }

  it should "consider negative numbers an invalid argument" in {
    evaluating {romanToArabic.toRoman(-1) } should produce [IllegalArgumentException]
    evaluating {romanToArabic.toRoman(-3) } should produce [IllegalArgumentException]
    evaluating {romanToArabic.toRoman(-100) } should produce [IllegalArgumentException]
  }
}