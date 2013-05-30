package com.pragmasoft.test.romannumber;


import java.util.Arrays;
import java.util.List;

public class RomanConverter {

    private static final SimpleRomanToken c = new SimpleRomanToken("C", 100, 3);
    private static final SimpleRomanToken xc = new SimpleRomanToken("XC", 90, 1);
    private static final SimpleRomanToken l = new SimpleRomanToken("L", 50, 1);
    private static final SimpleRomanToken xl = new SimpleRomanToken("XL", 40, 1);
    private static final SimpleRomanToken x = new SimpleRomanToken("X", 10, 3);
    private static final SimpleRomanToken ix = new SimpleRomanToken("IX", 9, 1);
    private static final SimpleRomanToken v = new SimpleRomanToken("V", 5, 1);
    private static final SimpleRomanToken iv = new SimpleRomanToken("IV", 4, 1);
    private static final SimpleRomanToken i = new SimpleRomanToken("I", 1, 3);

    private List<SimpleRomanToken> generators = Arrays.asList(
            c,
            xc,
            l,
            xl,
            x,
            ix,
            v,
            iv,
            i
    );

    private CompositeLinearParserToken parser = new CompositeLinearParserToken(
            c,
            new CompositeAlternativeToken(
                    xc,
                    new CompositeAlternativeToken(
                        xl,
                        new CompositeLinearParserToken(l, x)
                    )
                   ),
            new CompositeAlternativeToken(
                ix,
                new CompositeAlternativeToken(
                    iv,
                    new CompositeLinearParserToken(v, i)
                )
            )
    );


    public String toRoman(int number) {
        StringBuffer result = new StringBuffer();
        int remainder = number;
        for(RomanToken currToken : generators) {
            ValueAndRemainder currAccumulator = currToken.toRoman(remainder);
            result.append(currAccumulator.getValue());
            remainder = currAccumulator.getRemainder();
        }
        if(remainder != 0) {
            throw new IllegalStateException("Something went wrong, converting number " + number + " and returning " + result +
                    " with remainder " + remainder);
        }
        return result.toString();
    }

    public int fromRoman(String roman) {
        StringStream stream = new StringStream(roman);
        int result = parser.fromRoman(stream);
        if(!stream.isEof()) {
            throw new IllegalArgumentException("Invalid number '" + roman + "' parsed value is " + result);
        }

        return result;
    }
}
