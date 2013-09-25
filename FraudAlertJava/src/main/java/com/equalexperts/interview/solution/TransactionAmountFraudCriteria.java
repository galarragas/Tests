package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

import java.math.BigDecimal;

public class TransactionAmountFraudCriteria implements FraudCriteria {
    public static final String ALERT_MESSAGE = "Amount exceeded threshold.";

    private final long verificationWindow;
    private final BigDecimal maxTransactionAmount;

    public TransactionAmountFraudCriteria(long verificationWindow, BigDecimal maxTransactionAmount) {
        this.verificationWindow = verificationWindow;
        this.maxTransactionAmount = maxTransactionAmount;
    }

    @Override
    public boolean isPotentialFraud(Transaction transaction, TransactionList fullHistory) {
        return fullHistory.evaluateTransactionsInTimeWindow(
                transaction.getTimestamp(),
                verificationWindow,
                new TransactionVisitor<BigDecimal>() {
                    public BigDecimal visitTransaction(
                            Transaction currTransaction, BigDecimal currTransactionTotalAmount) {
                        return currTransactionTotalAmount.add( currTransaction.getAmount() );
                    }

                },
                new BigDecimal(0),
                new FailCriteria<BigDecimal>() {
                    @Override
                    public boolean shouldFail(BigDecimal currAccumulatedValue) {
                        return currAccumulatedValue.compareTo(maxTransactionAmount) > 0;
                    }
                }
        );
    }

    @Override
    public String getAlertMessage() {
        return ALERT_MESSAGE;
    }
}
