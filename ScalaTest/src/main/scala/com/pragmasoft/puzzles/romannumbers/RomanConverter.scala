package com.pragmasoft.puzzles.romannumbers

object RomanToIntConsole {
  def main (args : Array[String]) = {
    var romanConverter = new RomanConverter
    var currInput = ""
    do {
      currInput = readLine("Roman number:>")

      if (!currInput.isEmpty) {
        val (value, unparsed) = romanConverter.fromRoman(currInput)
        printf("Converted to %d - Unparsed input '%s'\n", value, unparsed)
      }
      else {
        println("Bye")
      }
    }
    while(!currInput.isEmpty)
  }
}


class RomanConverter {
  private val i = new SimpleRomanToken("I", 1 , 3)
  private val  iv = new SimpleRomanToken("IV", 4 , 1)
  private val  v = new SimpleRomanToken("V", 5 , 1)
  private val  ix = new SimpleRomanToken("IX", 9 , 1)
  private val  x = new SimpleRomanToken("X", 10 , 3)
  private val  xl = new SimpleRomanToken("XL", 40 , 1)
  private val  l = new SimpleRomanToken("L", 50 , 3)
  private val  xc = new SimpleRomanToken("XC", 90 , 1)
  private val  c = new SimpleRomanToken("C", 100 , 3)

  private val fourOrFiveAndOnes = new CompositeAlternativeTokenParser(iv, new ChainRomanTokenParser(v :: i :: List.empty))
  private val nineOrUnderTen = new CompositeAlternativeTokenParser(ix, fourOrFiveAndOnes)

  private val fortyNineOrFiftyAndTens = new CompositeAlternativeTokenParser(
      new ChainRomanTokenParser(xl :: nineOrUnderTen :: List.empty),
      new ChainRomanTokenParser(l :: x :: nineOrUnderTen :: List.empty)
  )
  private val ninetyOrUnderHundred = new CompositeAlternativeTokenParser(
    new ChainRomanTokenParser(xc :: nineOrUnderTen :: List.empty),
    fortyNineOrFiftyAndTens
  )

  private def parser = new ChainRomanTokenParser(c :: ninetyOrUnderHundred :: List.empty)
  private def generator = new ChainRomanTokenGenerator(c :: xc :: l :: xl :: x :: ix :: v :: iv :: i :: List.empty)

  def fromRoman(romanNumber : String) : (Int, String) = {
    var (value, parsedUntil) = parser.parse(romanNumber)

    (value, romanNumber.substring(parsedUntil))
  }

  def toRoman(value: Int) : String = generator.toRoman(value)._1
}
