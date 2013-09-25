package com.pragmasoft.tests.fraud

/**
 * Fraud alert service interface accepting
 *
 */
abstract trait FraudAlertService {
  /**
   * Submits an alert
   *
   * @param alert Alert to be submitted
   */
  def alert(alert: Alert)
}

/**
 * Simple interface for fraud detection service
 *
 */
abstract trait FraudDetectorService {

/**
 * Interface to submit new Transaction for processing. Implementation
 * needs to be thread-safe
 *
 * @param transaction Transaction to be submitted
 */
  def process(transaction: Transaction)

}
