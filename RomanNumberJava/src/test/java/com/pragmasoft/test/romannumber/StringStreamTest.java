package com.pragmasoft.test.romannumber;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringStreamTest {
    @Test
    public void shouldMatchNextChar() {
        assertTrue(new StringStream("XIV").matches("X"));
    }

    @Test
    public void shouldMatchNextCharSequence() {
        assertTrue(new StringStream("XLIV").matches("XL"));
    }

    @Test
    public void shouldNotMatchIfNotNextChar() {
        assertFalse(new StringStream("CXLIV").matches("XL"));
    }

    @Test
    public void shouldMoveForward() {
        assertTrue(new StringStream("CXLVII").consume(1).matches("X"));
    }

    @Test
    public void shouldMoveForward_moreMoves() {
        assertTrue(new StringStream("CXLVII").consume(1).consume(2).matches("V"));
    }

    @Test
    public void shouldDetectEof_moving() {
        assertTrue(new StringStream("CXLVII").consume(6).isEof());
    }

    @Test
    public void shouldDetectEof_empty() {
        assertTrue(new StringStream("").isEof());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfConsumingAfterEof() {
        new StringStream("abc").consume(3).consume(1);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfConsumingMoreThanAvailable() {
        new StringStream("abc").consume(4);
    }
}
