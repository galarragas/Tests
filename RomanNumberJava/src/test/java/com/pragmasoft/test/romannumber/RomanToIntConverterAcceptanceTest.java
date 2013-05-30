package com.pragmasoft.test.romannumber;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RomanToIntConverterAcceptanceTest {
    private RomanConverter romanConverter = new RomanConverter();

    @Test
    public void shouldConvertSimpleToken_unit() {
        assertEquals(1, romanConverter.fromRoman("I"));
    }
    @Test
    public void shouldConvertSimpleToken_five() {
        assertEquals(5, romanConverter.fromRoman("V"));
    }
    @Test
    public void shouldConvertSimpleToken_ten() {
        assertEquals(10, romanConverter.fromRoman("X"));
    }
    @Test
    public void shouldConvertSimpleToken_fifty() {
        assertEquals(50, romanConverter.fromRoman("L"));
    }

    @Test
    public void shouldConvertSimpleToken_hundredth() {
        assertEquals(100, romanConverter.fromRoman("C"));
    }

    @Test
    public void shouldConvertSpecialToken_four() {
        assertEquals(4, romanConverter.fromRoman("IV"));
    }

    @Test
    public void shouldConvertSpecialToken_nine() {
        assertEquals(9, romanConverter.fromRoman("IX"));
    }

    @Test
    public void shouldConvertSpecialToken_fourty() {
        assertEquals(40, romanConverter.fromRoman("XL"));
    }

    @Test
    public void shouldConvertSpecialToken_ninety() {
        assertEquals(90, romanConverter.fromRoman("XC"));
    }

    @Test
    public void shouldConvertRepeatedToken_units() {
        assertEquals(3, romanConverter.fromRoman("III"));
    }

    @Test
    public void shouldConvertRepeatedToken_tenths() {
        assertEquals(20, romanConverter.fromRoman("XX"));
    }

    @Test
    public void shouldConvertRepeatedToken_hundredths() {
        assertEquals(300, romanConverter.fromRoman("CCC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_four() {
        romanConverter.fromRoman("IVIV");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_five() {
        romanConverter.fromRoman("VV");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_nine() {
        romanConverter.fromRoman("IXIX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_fourty() {
        romanConverter.fromRoman("XLXL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_ninety() {
        romanConverter.fromRoman("XCXC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNotRepeatableTokensRepeated_fifty() {
        romanConverter.fromRoman("LL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForTooManyRepetitionsOfRepeatable_units() {
        romanConverter.fromRoman("IIII");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForTooManyRepetitionsOfRepeatable_tenths() {
        romanConverter.fromRoman("XXXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForTooManyRepetitionsOfRepeatable_hundredths() {
        romanConverter.fromRoman("CCCC");
    }

    @Test
    public void shouldConvertCompositeToken_noSpecials() {
        assertEquals(287, romanConverter.fromRoman("CCLXXXVII"));
    }

    @Test
    public void shouldConvertCompositeToken_specials() {
        assertEquals(199, romanConverter.fromRoman("CXCIX"));
    }

    @Test
    public void shouldConvertCompositeToken_mixed() {
        assertEquals(246, romanConverter.fromRoman("CCXLVI"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_fourAndUnits() {
        romanConverter.fromRoman("IVII");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_nineAndUnits() {
        romanConverter.fromRoman("IXII");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_nineAndFour() {
        romanConverter.fromRoman("IXIV");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_fourtyAndTen() {
        romanConverter.fromRoman("XLX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_fourtyAndFifty() {
        romanConverter.fromRoman("XLL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForIllegalCombination_ninetyAndTen() {
        romanConverter.fromRoman("XCX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForWrongOrder_startWithUnits() {
        romanConverter.fromRoman("IIXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForWrongOrder_compositeNumber() {
        romanConverter.fromRoman("XXXIVC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForUnrecognisedToken() {
        romanConverter.fromRoman("CCXXXZZIV");
    }
}
