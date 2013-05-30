package com.pragmasoft.test.romannumber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeLinearParserTokenTest {
    @Mock
    private RomanParserToken innerToken1;
    @Mock
    private RomanParserToken innerToken2;
    @Mock
    private RomanParserToken innerToken3;
    @Mock
    private StringStream stream;

    private CompositeLinearParserToken compositeLinearParserToken;

    @Before
    public void setUp() {
        compositeLinearParserToken = new CompositeLinearParserToken(innerToken1, innerToken2, innerToken3);

        when(innerToken1.fromRoman(any(StringStream.class))).thenReturn(1);
        when(innerToken2.fromRoman(any(StringStream.class))).thenReturn(2);
        when(innerToken3.fromRoman(any(StringStream.class))).thenReturn(3);
    }

    @Test
    public void shouldDelegateFromRomanToInternalTokens() {
        compositeLinearParserToken.fromRoman(stream);

        verify(innerToken1).fromRoman(same(stream));
        verify(innerToken2).fromRoman(same(stream));
        verify(innerToken3).fromRoman(same(stream));
    }

    @Test
    public void shouldReturnSumOfFromRomanResult() {
        assertEquals(6, compositeLinearParserToken.fromRoman(stream));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBuildWithEmptyList() {
        new CompositeLinearParserToken(new RomanToken[0]);
    }
}
