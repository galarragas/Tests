package com.pragmasoft.test.romannumber;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class IntToRomanConverterAcceptanceTest {
    private RomanConverter romanConverter = new RomanConverter();

    @Test
    public void shouldConvertSimpleTokens_unit() {
        assertEquals("I", romanConverter.toRoman(1));
    }

    @Test
    public void shouldConvertSimpleTokens_five() {
        assertEquals("V", romanConverter.toRoman(5));
    }

    @Test
    public void shouldConvertSimpleTokens_four() {
        assertEquals("IV", romanConverter.toRoman(4));
    }

    @Test
    public void shouldConvertSimpleTokens_nine() {
        assertEquals("IX", romanConverter.toRoman(9));
    }

    @Test
    public void shouldConvertSimpleTokens_ten() {
        assertEquals("X", romanConverter.toRoman(10));
    }

    @Test
    public void shouldConvertSimpleTokens_fourty() {
        assertEquals("XL", romanConverter.toRoman(40));
    }

    @Test
    public void shouldConvertSimpleTokens_fifty() {
        assertEquals("L", romanConverter.toRoman(50));
    }

    @Test
    public void shouldConvertSimpleTokens_ninety() {
        assertEquals("XC", romanConverter.toRoman(90));
    }

    @Test
    public void shouldConvertSimpleTokens_hundredth() {
        assertEquals("C", romanConverter.toRoman(100));
    }

    @Test
    public void shouldConvertRepeatedTokens_three() {
        assertEquals("III", romanConverter.toRoman(3));
    }

    @Test
    public void shouldConvertRepeatedTokens_thirty() {
        assertEquals("XXX", romanConverter.toRoman(30));
    }

    @Test
    public void shouldConvertRepeatedTokens_threeHundredth() {
        assertEquals("CCC", romanConverter.toRoman(300));
    }

    @Test
    public void shouldConvertCompositeTokens_fiveAndUnit() {
        assertEquals("VII", romanConverter.toRoman(7));
    }

    @Test
    public void shouldConvertCompositeTokens_tenAndFive() {
        assertEquals("XV", romanConverter.toRoman(15));
    }

    @Test
    public void shouldConvertCompositeTokens_tenAndFiveAndUnits() {
        assertEquals("XVIII", romanConverter.toRoman(18));
    }

    @Test
    public void shouldConvertCompositeTokens_tenAndNine() {
        assertEquals("XIX", romanConverter.toRoman(19));
    }

    @Test
    public void shouldConvertCompositeTokens_fiftyAndTen() {
        assertEquals("LXX", romanConverter.toRoman(70));
    }

    @Test
    public void shouldConvertCompositeTokens_fiftyAndTenAndUnits() {
        assertEquals("LXXVI", romanConverter.toRoman(76));
    }

    @Test
    public void shouldConvertCompositeTokens_fiftyAndTenAndFour() {
        assertEquals("LXIV", romanConverter.toRoman(64));
    }

    @Test
    public void shouldConvertCompositeTokens_hundredthAndFiftyAndTen() {
        assertEquals("CCLXX", romanConverter.toRoman(270));
    }

    @Test
    public void shouldConvertCompositeTokens_hundredthAndFiftyAndTenAndUnits() {
        assertEquals("CCCLXXIX", romanConverter.toRoman(379));
    }
}
