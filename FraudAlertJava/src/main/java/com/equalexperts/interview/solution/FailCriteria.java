package com.equalexperts.interview.solution;

public interface FailCriteria<T> {
    boolean shouldFail(T currAccumulatedValue);
}
