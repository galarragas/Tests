package com.pragmasoft.tests.fraud.solution

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

class ConcurrentMapTransactionStorageSpec extends FlatSpec with ShouldMatchers {

  "A ConcurrentMapTransactionStorage" should "create new element if none stored with given key" in {
    val storage = new ConcurrentMapTransactionStorage
    val transactionList = MutableTransactionList()

    storage.getOrElseUpdate("1234")(transactionList)

    storage.storage("1234") should be theSameInstanceAs(transactionList)
  }

  it should "return the element with given key if present" in {
    val storage = new ConcurrentMapTransactionStorage
    val transactionList = MutableTransactionList()

    storage.storage.put("1234", transactionList)

    storage.getOrElseUpdate("1234")(MutableTransactionList()) should be theSameInstanceAs(transactionList)
  }

  it should "leave the stored element with given key if present not replacing it" in {
    val storage = new ConcurrentMapTransactionStorage
    val transactionList = MutableTransactionList()

    storage.storage.put("1234", transactionList)

    storage.getOrElseUpdate("1234")(MutableTransactionList())

    storage.storage("1234") should be theSameInstanceAs(transactionList)
  }
}
