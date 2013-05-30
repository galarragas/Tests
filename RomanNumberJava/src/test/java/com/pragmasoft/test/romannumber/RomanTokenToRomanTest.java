package com.pragmasoft.test.romannumber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RomanTokenToRomanTest {

    @Test
    public void shouldRecogniseItself() {
        assertEquals(new ValueAndRemainder("I",0), new SimpleRomanToken("I", 1, 3).toRoman(1));
    }

    @Test
    public void shouldRecogniseItselfRepeated() {
        assertEquals(new ValueAndRemainder("III",0), new SimpleRomanToken("I", 1, 3).toRoman(3));
    }

    @Test
    public void shouldReturnTheRemainder() {
        assertEquals(new ValueAndRemainder("XXX",5), new SimpleRomanToken("X", 10, 3).toRoman(35));
    }

    @Test
    public void shouldReturnEmptyStringIfCannotMatch() {
        assertEquals(new ValueAndRemainder("",7), new SimpleRomanToken("X", 10, 3).toRoman(7));
    }
}
