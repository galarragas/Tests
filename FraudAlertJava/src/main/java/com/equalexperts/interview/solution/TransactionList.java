package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using a list of transactions sorted by time with more recent transactions first
 * <p/>
 * Is not implementing a purge logic to clean the not necessary transactions
 *
 * @author stefano
 */
public class TransactionList {

    private final LinkedList<Transaction> transactions;
    private final ReentrantReadWriteLock fLock = new ReentrantReadWriteLock();
    private final Lock readLock = fLock.readLock();
    private final Lock writeLock = fLock.writeLock();

    public TransactionList() {
        transactions = new LinkedList<Transaction>();
    }

    public void add(Transaction transactionToAdd) {
        writeLock.lock();
        try {
            ListIterator<Transaction> transactionsIterator = transactions.listIterator();

            boolean added = false;
            while (transactionsIterator.hasNext()) {
                Transaction currTransaction = transactionsIterator.next();

                if (currTransaction.getTimestamp().before(transactionToAdd.getTimestamp())) {
                    addBefore(transactionToAdd, transactionsIterator);
                    added = true;
                    break;
                }
            }

            if (!added) {
                transactions.addLast(transactionToAdd);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void addBefore(Transaction transactionToAdd,
                           ListIterator<Transaction> transactionsIterator) {
        if (transactionsIterator.hasPrevious()) {
            transactionsIterator.previous();
            transactionsIterator.add(transactionToAdd);
        } else {
            transactions.addFirst(transactionToAdd);
        }
    }

    public <T> boolean evaluateTransactionsInTimeWindow(Date startTime, long duration, TransactionVisitor<T> visitor, T startValue, FailCriteria<T> criteria) {

        // The window moves in the past
        Date endTime = new Date(startTime.getTime() - duration);

        readLock.lock();

        try {

            T currAccumulatedValue = startValue;
            for (Transaction currTransaction : transactions) {
                // after method requires the date to be strictly after
                if (currTransaction.getTimestamp().after(startTime)) {
                    continue;
                }

                if (currTransaction.getTimestamp().before(endTime)) {
                    break;
                }

                currAccumulatedValue = visitor.visitTransaction(currTransaction, currAccumulatedValue);

                if (criteria.shouldFail(currAccumulatedValue)) {
                    return true;
                }
            }

            return (criteria.shouldFail(currAccumulatedValue));
        } finally {
            readLock.unlock();
        }
    }
}
