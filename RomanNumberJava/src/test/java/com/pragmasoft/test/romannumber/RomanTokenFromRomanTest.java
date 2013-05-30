package com.pragmasoft.test.romannumber;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RomanTokenFromRomanTest {
    @Test
    public void shouldRecogniseItself() {
        assertEquals(1, new SimpleRomanToken("I", 1, 3).fromRoman(new StringStream("I")));
    }

    @Test
    public void shouldRecogniseItself_compositeToken() {
        assertEquals(40, new SimpleRomanToken("XL", 40, 1).fromRoman(new StringStream("XL")));
    }

    @Test
    public void shouldRecogniseItselfRepeated() {
        assertEquals(3, new SimpleRomanToken("I", 1, 3).fromRoman(new StringStream("III")));
    }

    @Test
    public void shouldStopAtMaxRepetitions() {
        assertEquals(3, new SimpleRomanToken("I", 1, 3).fromRoman(new StringStream("IIII")));
    }

    @Test
    public void shouldStopAtMaxRepetitions_streamAtNextChar() {
        final StringStream stream = new StringStream("IIII");
        assertEquals(3, new SimpleRomanToken("I", 1, 3).fromRoman(stream));
        assertTrue(stream.matches("I"));
        assertFalse(stream.matches("II"));
    }

    @Test
    public void shouldConsumeCharsToEOF() {
        final StringStream stream = new StringStream("II");
        new SimpleRomanToken("I", 1, 3).fromRoman(stream);
        assertTrue(stream.isEof());
    }
}
