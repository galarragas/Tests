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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeAlternativeTokenTest {
    @Mock
    private RomanParserToken firstToken;
    @Mock
    private RomanParserToken secondToken;
    @Mock
    private StringStream stream;

    private CompositeAlternativeToken compositeAlternativeToken;

    @Before
    public void setUp() {
        compositeAlternativeToken = new CompositeAlternativeToken(firstToken, secondToken);
    }

    @Test
    public void shouldCallFirstDelegateOnlyIfItCanConsume() {
        when(firstToken.fromRoman(any(StringStream.class))).thenReturn(10);

        compositeAlternativeToken.fromRoman(stream);

        verify(firstToken).fromRoman(same(stream));
        verifyZeroInteractions(secondToken);
    }

    @Test
    public void shouldCallReturnFirstDelegateResultIfItCanConsume() {
        when(firstToken.fromRoman(any(StringStream.class))).thenReturn(10);

        assertEquals(10, compositeAlternativeToken.fromRoman(stream));
    }

    @Test
    public void shouldCallSecondDelegateIfFirstCannotConsume() {
        when(firstToken.fromRoman(any(StringStream.class))).thenReturn(0);

        compositeAlternativeToken.fromRoman(stream);

        verify(firstToken).fromRoman(same(stream));
        verify(secondToken).fromRoman(same(stream));
    }

    @Test
    public void shouldCallReturnSecondDelegateResultIfFirstCannotConsume() {
        when(firstToken.fromRoman(any(StringStream.class))).thenReturn(0);
        when(secondToken.fromRoman(any(StringStream.class))).thenReturn(2);

        assertEquals(2, compositeAlternativeToken.fromRoman(stream));
    }
}
