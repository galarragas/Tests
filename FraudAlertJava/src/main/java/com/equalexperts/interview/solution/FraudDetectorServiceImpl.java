package com.equalexperts.interview.solution;

import com.equalexperts.interview.Alert;
import com.equalexperts.interview.FraudAlertService;
import com.equalexperts.interview.FraudDetectorService;
import com.equalexperts.interview.Transaction;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FraudDetectorServiceImpl implements FraudDetectorService {

	private final ConcurrentHashMap<String, TransactionList> transactionStorage;
	private final List<FraudCriteria> fraudCriteria;
	private final FraudAlertService fraudAlertService;

	public FraudDetectorServiceImpl(FraudAlertService fraudAlertService, List<FraudCriteria> fraudCriteria) {
		this(new ConcurrentHashMap<String, TransactionList>(), fraudAlertService, fraudCriteria);
	}

	FraudDetectorServiceImpl(
			ConcurrentHashMap<String, TransactionList> transactionStorage,
            FraudAlertService fraudAlertService,
            List<FraudCriteria> fraudCriteria) {
		this.transactionStorage = transactionStorage;
        this.fraudAlertService = fraudAlertService;
        this.fraudCriteria = fraudCriteria;
	}

	public void process(final Transaction transaction) {

		TransactionList emptyTransactionList = new TransactionList();
		TransactionList currCreditCardTransactions = transactionStorage.putIfAbsent(transaction.getCreditCardNumber(),
                emptyTransactionList);

		if (currCreditCardTransactions == null) {
			currCreditCardTransactions = emptyTransactionList;
		}

		currCreditCardTransactions.add(transaction);

        for(FraudCriteria currCriteria : fraudCriteria) {
            if(currCriteria.isPotentialFraud(transaction, currCreditCardTransactions)) {
                fraudAlertService.alert(new Alert(transaction, currCriteria.getAlertMessage()));
            }
        }
	}
}
