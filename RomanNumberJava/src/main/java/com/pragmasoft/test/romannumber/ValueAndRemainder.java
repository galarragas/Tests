package com.pragmasoft.test.romannumber;

public class ValueAndRemainder {
    private final int remainder;
    private final String value;

    public ValueAndRemainder(String value, int remainder) {
        this.value = value;
        this.remainder = remainder;
    }

    public String getValue() {
        return value;
    }

    public int getRemainder() {
        return remainder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueAndRemainder that = (ValueAndRemainder) o;

        if (remainder != that.remainder) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remainder;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ValueAndRemainder{" +
                "remainder=" + remainder +
                ", value='" + value + '\'' +
                '}';
    }
}
