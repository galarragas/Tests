package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;
import com.equalexperts.interview.solution.FailCriteria;
import com.equalexperts.interview.solution.TransactionList;
import com.equalexperts.interview.solution.TransactionVisitor;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.sort;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.junit.Assert.assertEquals;


public class TransactionListTest {

    static final int MIN_TIME = 10;
    static final int MAX_TIME = 19;
    private TransactionList transactionList;

    @Before
    public void setUp() {
        transactionList = new TransactionList();
    }

    @Test
    public void shouldAddInReverseOrder_orderedInput() {
        addTransactions();

        assertEquals(
                Arrays.asList(
                        new Transaction[]{
                                createTransactionAt(MIN_TIME + 6),
                                createTransactionAt(MIN_TIME + 5),
                                createTransactionAt(MIN_TIME + 4),
                                createTransactionAt(MIN_TIME + 3)
                        }),
                getTransactionsInInterval(transactionList, new Date(MIN_TIME + 6), 3)
        );
    }



    @Test
    public void shouldAddInReverseOrder_unorderedInput() {
        addTransactions();

        assertEquals(
                Arrays.asList(
                        new Transaction[]{
                                createTransactionAt(MIN_TIME + 6),
                                createTransactionAt(MIN_TIME + 5),
                                createTransactionAt(MIN_TIME + 4),
                                createTransactionAt(MIN_TIME + 3)
                        }),
                getTransactionsInInterval(transactionList, new Date(MIN_TIME + 6), 3)
        );
    }

    @Test
    public void shouldEvaluateExpressionInGivenRange_rangeFullyInExistingList() {
        addTransactions();

        assertEquals(Arrays.asList(
                        new Transaction[]{
                                createTransactionAt(MIN_TIME + 6),
                                createTransactionAt(MIN_TIME + 5),
                                createTransactionAt(MIN_TIME + 4),
                                createTransactionAt(MIN_TIME + 3)
                        }),
                getTransactionsInInterval(transactionList, new Date(MIN_TIME + 6), 3)
            );
    }

    @Test
    public void shouldEvaluateExpressionInGivenRange_rangeBottomNotInExistingList() {
        addTransactions();

        assertEquals(Arrays.asList(
                new Transaction[]{
                        createTransactionAt(MIN_TIME + 6),
                        createTransactionAt(MIN_TIME + 5),
                        createTransactionAt(MIN_TIME + 4),
                        createTransactionAt(MIN_TIME + 3),
                        createTransactionAt(MIN_TIME + 2),
                        createTransactionAt(MIN_TIME + 1),
                        createTransactionAt(MIN_TIME)
                }),
                getTransactionsInInterval(transactionList, new Date(MIN_TIME + 6), 10)
        );
    }

    @Test
    public void shouldEvaluateExpressionInGivenRange_rangeTopNotInExistingList() {
        addTransactions();

        assertEquals(Arrays.asList(
                new Transaction[]{
                        createTransactionAt(MAX_TIME),
                        createTransactionAt(MAX_TIME - 1),
                        createTransactionAt(MAX_TIME - 2)
                }),
                getTransactionsInInterval(transactionList, new Date(MAX_TIME + 1), 3)
        );
    }

    private List<Transaction> getTransactionsInInterval(TransactionList transactionList, Date date, int interval) {
        // Working on side effects on the array list
        final ArrayList<Transaction> result = new ArrayList<Transaction>();
        transactionList.evaluateTransactionsInTimeWindow(date, interval,
                accumulator(),
                result,
                neverFails()
        );
        return result;
    }

    private FailCriteria<List<Transaction>> neverFails() {
        return new FailCriteria<List<Transaction>>() {
            @Override
            public boolean shouldFail(List<Transaction> currAccumulatedValue) {
                return false;
            }
        };
    }

    private TransactionVisitor<List<Transaction>> accumulator() {
        return new TransactionVisitor<List<Transaction>>() {
            @Override
            public List<Transaction> visitTransaction(Transaction currTransaction, List<Transaction> currResultValue) {
                currResultValue.add(currTransaction);
                return currResultValue;
            }
        };
    }

	@Test
	public void testConcurrentUsage() throws InterruptedException, BrokenBarrierException {
		Transaction cc1Transactions[] = new Transaction[] {
                createTransactionAt(101l),
                createTransactionAt(120l),
                createTransactionAt(100l),
                createTransactionAt(110l),
                createTransactionAt(103l),
                createTransactionAt(105l),
                createTransactionAt(106l),
                createTransactionAt(112l)
			};

		Random rand = new Random();
		ExecutorService pool = newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(cc1Transactions.length + 1);
		for( final Transaction currTransaction : cc1Transactions) {
			pool.execute( new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();

                        transactionList.add(currTransaction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
          );
		}
		barrier.await();

        final Transaction[] sortedTransactions = Arrays.copyOf(cc1Transactions, cc1Transactions.length);
        sort(sortedTransactions, sortByDate());
        assertEquals(
                Arrays.asList(sortedTransactions),
                getTransactionsInInterval(transactionList, new Date(120l), 100)
        );
	}

    private Comparator<Transaction> sortByDate() {
        return new Comparator<Transaction>() {
            @Override
            public int compare(Transaction left, Transaction right) {
                return -1 * left.getTimestamp().compareTo(right.getTimestamp());
            }
        };
    }

    private void addTransactions() {
        for (long time = MIN_TIME; time <= MAX_TIME; time++) {
            transactionList.add(createTransactionAt(time));
        }
    }

    private void addTransactionsInverseOrder() {
        for (long time = MAX_TIME; time >= MIN_TIME; time--) {
            transactionList.add(createTransactionAt(time));
        }
    }

    private void addUnorderedTransactions() {
        for (long time = MIN_TIME; time <= MAX_TIME; time += 2) {
            transactionList.add(createTransactionAt(time));
        }

        for (long time = MAX_TIME; time > MIN_TIME; time -= 2) {
            transactionList.add(createTransactionAt(time));
        }
    }

    private Transaction createTransactionAt(long time) {
        return new Transaction("cc1", new Date(time), new BigDecimal(1));
    }
}
