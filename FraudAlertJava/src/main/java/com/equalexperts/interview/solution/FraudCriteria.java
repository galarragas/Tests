package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

public interface FraudCriteria {
    public boolean isPotentialFraud(Transaction transaction, TransactionList history);
    public String getAlertMessage();
}
