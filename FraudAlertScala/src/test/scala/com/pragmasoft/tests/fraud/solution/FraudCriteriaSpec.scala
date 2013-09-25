package com.pragmasoft.tests.fraud.solution

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

class FraudCriteriaSpec extends FlatSpec with ShouldMatchers {

  trait WithNotificationSpy {
    var notificationCount: Int = 0
    var notificationMessages: List[String]

    val notificationSpy: String => Unit = message => {
      notificationCount = notificationCount + 1; notificationMessages = message :: notificationMessages
    }
  }

  "A TransactionCountFraudCriteria" should "detect fraud for too many transactions in interval" in {
    new WithNotificationSpy {
      var notificationMessages: List[String] = _
      val criterium = new TransactionCountFraudCriteria(100, 1000l)

//      criterium.execIfPotentialFraud(transaction, history)(notificationSpy)

      notificationCount should be(1)
    }
  }
}
