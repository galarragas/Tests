package com.pragmasoft.tests.fraud.solution

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.pragmasoft.tests.fraud.Transaction
import org.joda.time.DateTime
import java.util.NoSuchElementException

class MutableTransactionListSpec extends FlatSpec with ShouldMatchers {


  def getTransactions(list: MutableTransactionList, from: DateTime, interval: Long) =
    list.foldInTimeRange[List[Transaction]](from, interval)(List.empty) { case (acc, transaction) => transaction :: acc }

  "GetTransactions" should "accumulate content of list" in {
    val list = List(Transaction(DateTime.now.plus(120), "123", BigDecimal(3)), Transaction(DateTime.now, "123", BigDecimal(1)), Transaction(DateTime.now.minus(100), "123", BigDecimal(2)))

    list.foldLeft[List[Transaction]](List.empty){ case (acc, transaction) => transaction :: acc } should be(list.reverse)
  }

  "A MutableTransactionListList" should "add transaction to the inner list" in {
    val transactionList = new MutableTransactionList

    val transaction = Transaction(DateTime.now, "123", BigDecimal(10))

    transactionList.add(transaction)

    transactionList.getTransactionsContent should be(List(transaction))
  }

  it should "add transactions in reverse time order" in {
    val transactionList = new MutableTransactionList

    val transaction = Transaction(DateTime.now, "123", BigDecimal(1))
    val transaction1 = Transaction(DateTime.now.minus(100), "123", BigDecimal(2))
    val transaction2 = Transaction(DateTime.now.plus(120), "123", BigDecimal(3))

    transactionList.add(transaction)
    transactionList.add(transaction1)
    transactionList.add(transaction2)

    transactionList.getTransactionsContent should be(List(transaction2, transaction, transaction1))
  }

  it should "fold content in time range, accumilating them" in {
    val transactionList = new MutableTransactionList

    val transaction = Transaction(DateTime.now, "123", BigDecimal(1))
    val transaction1 = Transaction(DateTime.now.minus(100), "123", BigDecimal(2))
    val transaction2 = Transaction(DateTime.now.plus(120), "123", BigDecimal(3))

    transactionList.add(transaction)
    transactionList.add(transaction1)
    transactionList.add(transaction2)

    getTransactions(transactionList, transaction2.time.plus(1), 400) should be(List(transaction1, transaction, transaction2))
  }

  it should "fold content in time range, counting them" in {
    val transactionList = new MutableTransactionList

    val transaction = Transaction(DateTime.now, "123", BigDecimal(1))
    val transaction1 = Transaction(DateTime.now.minus(100), "123", BigDecimal(2))
    val transaction2 = Transaction(DateTime.now.plus(120), "123", BigDecimal(3))

    transactionList.add(transaction)
    transactionList.add(transaction1)
    transactionList.add(transaction2)

    transactionList.foldInTimeRange(transaction2.time.plus(1), 400) (0) { case (acc, _) => acc + 1} should be(3)

  }
}
