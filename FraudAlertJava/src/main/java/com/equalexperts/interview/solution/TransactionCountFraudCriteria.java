package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

public class TransactionCountFraudCriteria implements FraudCriteria {
    public static final String ALERT_MESSAGE = "Number of transactions exceeded threshold.";

    private final long verificationWindow;
    private final Integer maxTransactionCount;

    public TransactionCountFraudCriteria(long verificationWindow, Integer maxTransactionCount) {
        this.verificationWindow = verificationWindow;
        this.maxTransactionCount = maxTransactionCount;
    }

    @Override
    public boolean isPotentialFraud(Transaction transaction, TransactionList history) {
        return history.evaluateTransactionsInTimeWindow(
                transaction.getTimestamp(),
                verificationWindow,
                new TransactionVisitor<Integer>() {
                    public Integer visitTransaction(
                            Transaction currTransaction, Integer currTransactionCount) {
                        return currTransactionCount + 1;
                    }

                },
                0,
                new FailCriteria<Integer>() {
                    @Override
                    public boolean shouldFail(Integer currAccumulatedValue) {
                        return currAccumulatedValue > maxTransactionCount;
                    }
                }
        );
    }

    @Override
    public String getAlertMessage() {
        return ALERT_MESSAGE;
    }
}
