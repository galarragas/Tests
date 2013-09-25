package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public abstract class TransactionGenerator implements Runnable {
	int transactionCount;
	Transaction specialTransaction;
	CyclicBarrier barrier;
	String cardNameSeed;
	int specialTransactionIndex;
	
	public TransactionGenerator( String cardnameSeed, Transaction specialOne, int specialTransactionIndex, int transactionCount, CyclicBarrier barrier ) {
		this.specialTransaction = specialOne;
		this.barrier = barrier;
		this.cardNameSeed = cardnameSeed;
		this.transactionCount = transactionCount;
		this.specialTransactionIndex = specialTransactionIndex;
	}
	
	protected abstract void processTransaction( Transaction currTransaction );
	
	public void run() {
		try {
			for( int i = 0; i < transactionCount; i++ ) {
				if( i == specialTransactionIndex ) {
					processTransaction(specialTransaction);
				}
				else {
					processTransaction( new Transaction( cardNameSeed + i, new Date(i), new BigDecimal(1) ) );
				}
			}
		}
		finally {
			try {
				barrier.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (BrokenBarrierException e) {
				// Ignored
			}
		}
	}
	
}