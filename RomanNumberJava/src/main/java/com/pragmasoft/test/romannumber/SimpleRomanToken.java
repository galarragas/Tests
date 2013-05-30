package com.pragmasoft.test.romannumber;

public class SimpleRomanToken implements RomanToken {
    private final String symbol;
    private final int value;
    private final int maxRepetitions;

    public SimpleRomanToken(String symbol, int value, int maxRepetitions) {
        this.symbol = symbol;
        this.value = value;
        this.maxRepetitions = maxRepetitions;
    }

    @Override
    public ValueAndRemainder toRoman(int toConvert) {
        StringBuffer result = new StringBuffer();

        int remainder = toConvert;
        while(remainder >= value) {
            result.append(symbol);
            remainder -= value;
        }

        return new ValueAndRemainder(result.toString(), remainder);
    }

    @Override
    public int fromRoman(StringStream stream) {
        int result = 0;
        int repetitions = 0;
        while((repetitions < maxRepetitions) && stream.matches(symbol)) {
            result += value;
            stream.consume(symbol.length());
            repetitions++;
        }
        return result;
    }
}
