package com.pragmasoft.test.romannumber;

import java.util.Arrays;
import java.util.List;

public class CompositeLinearParserToken implements RomanParserToken {
    private final List<RomanParserToken> delegateList;

    public CompositeLinearParserToken(RomanParserToken... delegate) {
        if(delegate.length == 0) {
            throw new IllegalArgumentException("Need at least one delegate");
        }

        this.delegateList = Arrays.asList(delegate);
    }

    @Override
    public int fromRoman(StringStream stream) {
        int result = 0;
        for(RomanParserToken currDelegate : delegateList) {
            result += currDelegate.fromRoman(stream);
        }

        return result;
    }
}
