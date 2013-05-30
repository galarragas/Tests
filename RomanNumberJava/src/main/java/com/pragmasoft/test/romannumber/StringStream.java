package com.pragmasoft.test.romannumber;

public class StringStream {
    private final String content;
    private int position;

    public StringStream(String content) {
        this.content = content;
        this.position = 0;
    }

    public boolean isEof() {
        return position == content.length();
    }

    public boolean matches(String toMatch) {
        return content.startsWith(toMatch, position);
    }

    public StringStream consume(int lenght) {
        if((position + lenght) > content.length()) {
            throw new IllegalStateException("Trying to consume after EOF");
        }

        position += lenght;

        return this;
    }
}
