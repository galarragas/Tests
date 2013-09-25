package com.equalexperts.interview.solution;

import com.equalexperts.interview.Transaction;

public interface TransactionVisitor<T> {
	T visitTransaction( Transaction currTransaction, T currResultValue );
}
