package com.equalexperts.interview;

/**
 * Fraud alert service interface accepting
 * 
 * @author Expedia, Inc.
 */
public interface FraudAlertService {

	/**
	 * Submits an alert
	 * 
	 * @param alert Alert to be submitted
	 */
	public void alert(Alert alert);
}
