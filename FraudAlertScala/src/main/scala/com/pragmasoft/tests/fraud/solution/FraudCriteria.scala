package com.pragmasoft.tests.fraud.solution

import com.pragmasoft.tests.fraud.Transaction

abstract trait FraudCriteria {
  protected def fraudMessage : String
  protected def isPotentialFraud(currentTransaction: Transaction, history: TransactionList) : Boolean

  def execIfPotentialFraud(currentTransaction: Transaction, history: TransactionList)(notification: String => Unit) : Unit =
      if(isPotentialFraud(currentTransaction, history))
        notification(fraudMessage)
}

object TransactionCountFraudCriteria {
  val MESSAGE = "Too many transactions in interval"

  def apply(threashold: Int, interval: Long) = new TransactionCountFraudCriteria(threashold, interval)
}

class TransactionCountFraudCriteria(threashold: Int, interval: Long) extends FraudCriteria {
  import TransactionCountFraudCriteria._
  protected def fraudMessage: String = MESSAGE

  protected def isPotentialFraud(currentTransaction: Transaction, history: TransactionList): Boolean =
      history.foldInTimeRange(currentTransaction.time, interval)(0) { case (count, _) => count + 1 } >= threashold
}
object TransactionAmountFraudCriteria {
  val MESSAGE = "Transaction for a too big total amount in interval"

  def apply(threashold: BigDecimal, interval: Long) = new TransactionAmountFraudCriteria(threashold, interval)
}

class TransactionAmountFraudCriteria(threashold: BigDecimal, interval: Long) extends FraudCriteria {
  import TransactionAmountFraudCriteria._
  protected def fraudMessage: String = MESSAGE

  protected def isPotentialFraud(currentTransaction: Transaction, history: TransactionList): Boolean =
    history.foldInTimeRange(currentTransaction.time, interval)(BigDecimal(0)) { case (acc, currTrans) => acc + currTrans.amount } >= threashold
}

