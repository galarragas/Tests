package com.equalexperts.interview;

/**
 * Simple interface for fraud detection service
 * 
 * @author Expedia, Inc.
 */
public interface FraudDetectorService {

	/**
	 * Interface to submit new Transaction for processing. Implementation
	 * needs to be thread-safe
	 * 
	 * @param transaction Transaction to be submitted
	 */
	public void process(Transaction transaction);
	
}
