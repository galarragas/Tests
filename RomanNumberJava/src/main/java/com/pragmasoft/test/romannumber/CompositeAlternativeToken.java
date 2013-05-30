package com.pragmasoft.test.romannumber;

public class CompositeAlternativeToken implements RomanParserToken {
    private final RomanParserToken firstDelegate;
    private final RomanParserToken secondDelegate;

    public CompositeAlternativeToken(RomanParserToken firstToken, RomanParserToken secondToken) {
        this.firstDelegate = firstToken;
        this.secondDelegate = secondToken;
    }

    @Override
    public int fromRoman(StringStream stream) {
        int firstResult = firstDelegate.fromRoman(stream);

        if(firstResult > 0) {
            return firstResult;
        }

        return secondDelegate.fromRoman(stream);
    }
}
