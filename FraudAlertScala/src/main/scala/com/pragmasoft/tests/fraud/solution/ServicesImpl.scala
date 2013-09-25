package com.pragmasoft.tests.fraud.solution

import com.pragmasoft.tests.fraud._

import com.github.nscala_time.time.Imports._
import java.util.concurrent.locks.{Lock, ReentrantReadWriteLock}
import scala.collection.mutable.ListBuffer
import java.util.concurrent.ConcurrentHashMap
import java.lang.Math.max
import org.joda.time

// LRU list of transactions
trait TransactionList {
  def add(transaction: Transaction)

  def foldInTimeRange[T](mostRecent: DateTime, windowSizeInMillis: Long)(startValue: T)(f: (T, Transaction) => T) : T
}

class MutableTransactionList extends TransactionList {
  val transactions = ListBuffer[Transaction]()

  val fLock: ReentrantReadWriteLock = new ReentrantReadWriteLock()
  val readLock: Lock = fLock.readLock()
  val writeLock: Lock = fLock.writeLock()

  def withWriteLock[T] (f: => T) : T = { writeLock.lock(); try { f } finally { writeLock.unlock() } }
  def withReadLock[T] (f: => T) : T= { readLock.lock(); try { f } finally { readLock.unlock() } }

  def add(transaction: Transaction) = {
    withWriteLock {
      val insertIndex: Int = transactions.lastIndexWhere(_.time.isAfter(transaction.time))
      transactions.insert(insertIndex + 1, transaction)
    }
  }


  def foldInTimeRange[T](mostRecent: DateTime, windowSizeInMillis: Long)(startValue: T)(f: (T, Transaction) => T) : T = {
    def isInRange(transaction: Transaction, min: DateTime, max: DateTime) = !(transaction.time.isBefore(min) || transaction.time.isAfter(max))

    withReadLock{
      transactions.filter(isInRange(_, mostRecent.minus(windowSizeInMillis), mostRecent)).foldLeft(startValue)(f)
    }
  }

  protected[solution] def setTransactionsContent(newContent: List[Transaction]) = {
    withWriteLock {
      transactions.clear()
      transactions.appendAll(newContent)
    }
  }

  protected[solution] def getTransactionsContent = withReadLock(transactions.toList)
}

object MutableTransactionList {
  def apply() = new MutableTransactionList
}

abstract trait ConcurrentTransactionStorage {
  def getOrElseUpdate(creditCardNumber: String)(op: ⇒ TransactionList): TransactionList
}

class ConcurrentMapTransactionStorage extends ConcurrentTransactionStorage {
  protected[solution] val storage: scala.collection.concurrent.Map[String, TransactionList] = new scala.collection.concurrent.TrieMap[String, TransactionList]()

  def getOrElseUpdate(creditCardNumber: String)(op: ⇒ TransactionList): TransactionList = storage.getOrElseUpdate(creditCardNumber, op)
}

abstract trait FraudDetectorServiceImpl extends FraudDetectorService {
  def transactionStorage: ConcurrentTransactionStorage
  def fraudDetectionCriteria: List[FraudCriteria]
  def fraudAlertService: FraudAlertService

  def process(transaction: Transaction) = {
    val transactionHistory = transactionStorage.getOrElseUpdate(transaction.cardNumber) {
      MutableTransactionList()
    }

    transactionHistory.add(transaction)

    fraudDetectionCriteria.foreach(
      _.execIfPotentialFraud(transaction, transactionHistory) {
        alertMsg => fraudAlertService.alert(Alert(transaction, alertMsg))
      }
    )
  }
}
