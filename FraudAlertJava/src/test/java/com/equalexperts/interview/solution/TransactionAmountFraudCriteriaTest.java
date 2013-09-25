package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;
import com.equalexperts.interview.solution.TransactionAmountFraudCriteria;
import com.equalexperts.interview.solution.TransactionList;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionAmountFraudCriteriaTest {
    private final TransactionList fullTransactionList = new TransactionList();
    private Transaction secondTransaction;
    private Transaction newestTransaction;
    private TransactionAmountFraudCriteria criteria;
    private Transaction oldTransaction;

    @Before
    public void setUp() {
        fullTransactionList.add(new Transaction("cc1", new Date(120l), new BigDecimal(100)));
        fullTransactionList.add(new Transaction("cc1", new Date(124l), new BigDecimal(800)));
        oldTransaction = new Transaction("cc1", new Date(126l), new BigDecimal(102l));
        fullTransactionList.add(oldTransaction);
        fullTransactionList.add(new Transaction("cc1", new Date(130l), new BigDecimal(100)));
        secondTransaction = new Transaction("cc1", new Date(140l), new BigDecimal(10000));
        fullTransactionList.add(secondTransaction);
        newestTransaction = new Transaction("cc1", new Date(151l), new BigDecimal(2l));
        fullTransactionList.add(newestTransaction);

        criteria = new TransactionAmountFraudCriteria(10l, new BigDecimal(1000l));
    }

    @Test
    public void shouldDetectFraudForTransactionWithPreviousHavingTooHighAmount() {
        assertTrue(criteria.isPotentialFraud(secondTransaction, fullTransactionList));
    }

    @Test
    public void shouldDetectFraudForSumOfTransactionsWithPreviousHavingTooHighAmount() {
        assertTrue(criteria.isPotentialFraud(oldTransaction, fullTransactionList));
    }

    @Test
    public void shouldNotDetectFraudForTransactionWithPreviousHavingTooHighAmountTooMuchBackInTime() {
        assertFalse(criteria.isPotentialFraud(newestTransaction, fullTransactionList));
    }

}
