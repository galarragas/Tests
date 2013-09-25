package com.pragmasoft.tests.fraud.solution

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import com.pragmasoft.tests.fraud.{Alert, FraudAlertService}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers.{eq => argEq}
import org.joda.time.{Duration, DateTime}
import org.joda.time.Duration._
import com.pragmasoft.tests.fraud.Transaction
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.mockito.Matchers
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock

class FraudDetectorServiceImplSpec extends FlatSpec with ShouldMatchers with MockitoSugar {

  abstract trait WithStorage {
    def mockedTransactionStorage: ConcurrentTransactionStorage
  }

  abstract trait WithCriteria {
    def mockedFraudDetectionCriteria: List[FraudCriteria]
  }

  trait StorageHavingHistory extends WithStorage {
    val currCardTransactions = mock[TransactionList]
    val mockedTransactionStorage = new ConcurrentTransactionStorage {
      def getOrElseUpdate(creditCardNumber: String)(op: => TransactionList): TransactionList = currCardTransactions
    }
  }

  trait WithMockedCriteria extends WithCriteria {
    val mockedFraudDetectionCriteria = List(mock[FraudCriteria], mock[FraudCriteria])
  }

  trait WithMockedServices {
    self: WithStorage with WithCriteria =>

    val mockedFraudAlertService = mock[FraudAlertService]

    val fraudDetectorService = new FraudDetectorServiceImpl {
      def transactionStorage: ConcurrentTransactionStorage = mockedTransactionStorage

      def fraudDetectionCriteria: List[FraudCriteria] = mockedFraudDetectionCriteria

      def fraudAlertService: FraudAlertService = mockedFraudAlertService
    }
  }

  trait MockedTestWithHistory extends WithMockedServices with StorageHavingHistory with WithMockedCriteria {}

  trait MockedTestWithHistoryAndFailingCriteria extends WithMockedServices with StorageHavingHistory with WithCriteria {
    val criteria = new FraudCriteria {
      protected def fraudMessage: String = "message"

      protected def isPotentialFraud(currentTransaction: Transaction, history: TransactionList): Boolean = true
    }

    def mockedFraudDetectionCriteria = List(criteria)
  }

  "A FraudDetectorServiceImpl" should "store incoming transaction" in {
    new MockedTestWithHistory {
      val transaction = Transaction(DateTime.now, "123", BigDecimal(12))

      fraudDetectorService.process(transaction)

      verify(currCardTransactions) add same(transaction)
    }
  }

  it should "invoke all criteria" in {
    new MockedTestWithHistory {
      val transaction = Transaction(DateTime.now, "123", BigDecimal(12))

      fraudDetectorService.process(transaction)

      mockedFraudDetectionCriteria.foreach(criteria => verify(criteria).execIfPotentialFraud(same(transaction), same(currCardTransactions))(anyObject()))
    }
  }

  it should "invoke fraudAlertService when a fraud criterium matches" in {
    new MockedTestWithHistoryAndFailingCriteria {
      val transaction = Transaction(DateTime.now, "123", BigDecimal(12))

      fraudDetectorService.process(transaction)

      verify(mockedFraudAlertService) alert argEq(Alert(transaction, "message"))
    }
  }

  def asMillis(amount: Int, unit: (Long => Duration)) = unit(amount) getMillis

  "A completely configured (excepted fraudAlertService) fraudDetectorService" should "notify the three cases in the example with the sample data" in {

    val mockedFraudAlertService = mock[FraudAlertService]

    val fraudDetectorService = new FraudDetectorServiceImpl {
      val transactionStorage = new ConcurrentMapTransactionStorage

      val fraudDetectionCriteria = List(
        TransactionAmountFraudCriteria(BigDecimal(10000), asMillis(30, standardMinutes)),
        TransactionCountFraudCriteria(3, asMillis(3, standardSeconds))
      )

      val fraudAlertService = mockedFraudAlertService
    }

    val dateFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSS")


    fraudDetectorService process Transaction(DateTime.parse("10:00:01.000", dateFormatter), "4111111111111111", BigDecimal(150.00))
    val firstAmountNotificationTx = Transaction(DateTime.parse("10:00:05.000", dateFormatter), "4111111111111111", BigDecimal(10000.00))
    fraudDetectorService process firstAmountNotificationTx
    fraudDetectorService process Transaction(DateTime.parse("10:00:10.000", dateFormatter), "4012888888881881", BigDecimal(150.00))
    fraudDetectorService process Transaction(DateTime.parse("10:00:11.000", dateFormatter), "4012888888881881", BigDecimal(150.00))
    val firstCountNotificationTx: Transaction = Transaction(DateTime.parse("10:00:12.000", dateFormatter), "4012888888881881", BigDecimal(150.00))
    fraudDetectorService process firstCountNotificationTx
    val secondCountNotificationTx: Transaction = Transaction(DateTime.parse("10:00:14.000", dateFormatter), "4012888888881881", BigDecimal(150.00))
    fraudDetectorService process secondCountNotificationTx
    val secondAmountNotificationTx: Transaction = Transaction(DateTime.parse("10:05:55.000", dateFormatter), "4111111111111111", BigDecimal(100.00))
    fraudDetectorService process secondAmountNotificationTx
    fraudDetectorService process Transaction(DateTime.parse("11:00:14.000", dateFormatter), "4012888888881881", BigDecimal(150.00))



    verify(mockedFraudAlertService).alert(Matchers.eq(Alert(firstAmountNotificationTx, TransactionAmountFraudCriteria.MESSAGE)) )
    verify(mockedFraudAlertService).alert(Matchers.eq(Alert(secondAmountNotificationTx, TransactionAmountFraudCriteria.MESSAGE)) )
    verify(mockedFraudAlertService).alert(Matchers.eq(Alert(firstCountNotificationTx, TransactionCountFraudCriteria.MESSAGE)) )
    verify(mockedFraudAlertService).alert(Matchers.eq(Alert(secondCountNotificationTx, TransactionCountFraudCriteria.MESSAGE)) )

    verifyNoMoreInteractions(mockedFraudAlertService)
  }
}
