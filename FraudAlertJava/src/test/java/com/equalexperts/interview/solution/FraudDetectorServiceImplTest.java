package com.equalexperts.interview.solution;

import com.equalexperts.interview.Alert;
import com.equalexperts.interview.FraudAlertService;
import com.equalexperts.interview.Transaction;
import com.equalexperts.interview.solution.FraudCriteria;
import com.equalexperts.interview.solution.FraudDetectorServiceImpl;
import com.equalexperts.interview.solution.TransactionList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class FraudDetectorServiceImplTest {

    static final String CRITERIA_1_MESSAGE = "Criteria 1 message";
    static final String CRITERIA_2_MESSAGE = "Criteria 2 message";

    @Mock
    TransactionList creditCard1_transactions;
	@Mock TransactionList creditCard2_transactions;
	@Mock ConcurrentHashMap<String, TransactionList> transactionStorage;

	@Mock
    FraudAlertService fraudAlertService;

    @Mock
    FraudCriteria mockCriteria1;
    @Mock FraudCriteria mockCriteria2;

	private FraudDetectorServiceImpl fraudDetectorService;

    private static final String CREDIT_CARD_1_ID = "cc1";
    private static final String CREDIT_CARD_2_ID = "cc2";


	@Before
	public void setUp() {
		given(transactionStorage.putIfAbsent(eq(CREDIT_CARD_1_ID), any(TransactionList.class))).willReturn(creditCard1_transactions);
		given(transactionStorage.putIfAbsent(eq(CREDIT_CARD_2_ID), any(TransactionList.class))).willReturn(creditCard2_transactions);

        given(mockCriteria1.getAlertMessage()).willReturn(CRITERIA_1_MESSAGE);
        given(mockCriteria2.getAlertMessage()).willReturn(CRITERIA_2_MESSAGE);

		fraudDetectorService = new FraudDetectorServiceImpl( transactionStorage, fraudAlertService,
                Arrays.asList(new FraudCriteria[]{mockCriteria1, mockCriteria2})
            );
	}

	@Test
	public void testTransactionIsAdded() {
		Transaction testTransaction = new Transaction(CREDIT_CARD_1_ID, new Date( 800L ), new BigDecimal(200) );
		fraudDetectorService.process(testTransaction);
		
		verify(transactionStorage).putIfAbsent( eq(CREDIT_CARD_1_ID), any(TransactionList.class) );
		verify(creditCard1_transactions).add( testTransaction );
	}

	@Test
	public void shouldVerifyAllCriteria() {
        Transaction testTransaction = new Transaction(CREDIT_CARD_1_ID, new Date( 800L ), new BigDecimal(200) );

        fraudDetectorService.process(testTransaction);

        verify(mockCriteria1).isPotentialFraud(same(testTransaction), same(creditCard1_transactions));
        verify(mockCriteria2).isPotentialFraud(same(testTransaction), same(creditCard1_transactions));
	}

    @Test
    public void shouldEvaluateAllCriteriaInCaseOfMatch() {
        given(mockCriteria1.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(true);
        given(mockCriteria2.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(false);

        Transaction testTransaction = new Transaction(CREDIT_CARD_1_ID, new Date( 800L ), new BigDecimal(200) );

        fraudDetectorService.process(testTransaction);

        verify(mockCriteria2).isPotentialFraud(eq(testTransaction), same(creditCard1_transactions));
    }

    @Test
    public void shouldNotGenerateAlertIfCriteriaAreNotMatched() {
        given(mockCriteria2.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(false);
        given(mockCriteria1.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(false);

        Transaction testTransaction = new Transaction(CREDIT_CARD_1_ID, new Date( 800L ), new BigDecimal(200) );

        fraudDetectorService.process(testTransaction);

        verifyNoMoreInteractions(fraudAlertService);
    }

	@Test
	public void shouldGenerateAlertForMatchedCriteria() {
        given(mockCriteria1.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(false);
        given(mockCriteria2.isPotentialFraud(any(Transaction.class), any(TransactionList.class))).willReturn(true);

        Transaction testTransaction = new Transaction(CREDIT_CARD_1_ID, new Date( 800L ), new BigDecimal(200) );

		fraudDetectorService.process(testTransaction);

        verify(fraudAlertService).alert(eq(new Alert(testTransaction, CRITERIA_2_MESSAGE)));
        verifyNoMoreInteractions(fraudAlertService);
	}



}
