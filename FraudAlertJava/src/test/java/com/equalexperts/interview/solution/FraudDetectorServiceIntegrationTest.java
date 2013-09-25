package com.equalexperts.interview.solution;

import com.equalexperts.interview.Alert;
import com.equalexperts.interview.FraudAlertService;
import com.equalexperts.interview.Transaction;
import com.equalexperts.interview.solution.FraudCriteria;
import com.equalexperts.interview.solution.FraudDetectorServiceImpl;
import com.equalexperts.interview.solution.TransactionAmountFraudCriteria;
import com.equalexperts.interview.solution.TransactionCountFraudCriteria;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FraudDetectorServiceIntegrationTest {
    @Mock
    FraudAlertService fraudAlertService;

    private static final String CREDIT_CARD_1_ID = "cc1";
    private static final String CREDIT_CARD_2_ID = "cc2";

    private final FraudCriteria amountCriteria = new TransactionAmountFraudCriteria(30 * 60 * 1000L, new BigDecimal(10000));
    private final FraudCriteria countCriteria = new TransactionCountFraudCriteria(30000L, 3);

    private FraudDetectorServiceImpl fraudDetectorService;

    @Before
    public void setUp() {
        fraudDetectorService = new FraudDetectorServiceImpl(fraudAlertService, Arrays.asList(new FraudCriteria[]{amountCriteria, countCriteria}));
    }

    // Testing the addition in parallel of the transactions.
    // NOTE:
    // The transactions are out of order since we can't assume that the fraudDetectorService is called respecting the time sequence of the
    // transactions because of the multiple callers and the nature of the payment system behind it.
    @Ignore // Not reliable enough
    @Test
    public void testParallel() throws InterruptedException, BrokenBarrierException {
        Transaction transactions[] = new Transaction[] {
                new Transaction( "4111111111111111", new Date(2012, 01, 18, 10, 0, 1 ), new BigDecimal(150) ),
                new Transaction("4111111111111111", new Date(2012, 01, 18, 10, 0, 5), new BigDecimal(10000)),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 10 ), new BigDecimal(150) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 11 ), new BigDecimal(150) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 12 ), new BigDecimal(150) ),
                new Transaction("4012888888881881", new Date(2012, 01, 18, 10, 0, 14), new BigDecimal(150)),
                new Transaction( "4111111111111111", new Date(2012, 01, 18, 10, 0, 55 ), new BigDecimal(100) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 11, 0, 14 ), new BigDecimal(150) )
        };

        final Random rand = new Random();
        ExecutorService pool = newFixedThreadPool(transactions.length / 2);
        for( final Transaction currentTransaction : transactions ) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(rand.nextInt(2) + 1);

                        fraudDetectorService.process(currentTransaction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(20l, TimeUnit.SECONDS);

        verify(fraudAlertService, Mockito.times(3)).alert( any(Alert.class));
    }


    // This is the test reproducing the single threaded test in the README.txt file
    @Test
    public void multiNotificationTest() {

        // can't use IODA time
        @SuppressWarnings("deprecation")
        Transaction[] inputData = new Transaction[] {
                new Transaction( "4111111111111111", new Date(2012, 01, 18, 10, 0, 1 ), new BigDecimal(150) ),
                new Transaction( "4111111111111111", new Date(2012, 01, 18, 10, 0, 5 ), new BigDecimal(10000) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 10 ), new BigDecimal(150) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 11 ), new BigDecimal(150) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 12 ), new BigDecimal(150) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 10, 0, 14 ), new BigDecimal(150) ),
                new Transaction( "4111111111111111", new Date(2012, 01, 18, 10, 0, 55 ), new BigDecimal(100) ),
                new Transaction( "4012888888881881", new Date(2012, 01, 18, 11, 0, 14 ), new BigDecimal(150) )

        };

        Alert[] expectedAlerts = new Alert[] {
                new Alert( inputData[1], amountCriteria.getAlertMessage() ),
                new Alert( inputData[5], countCriteria.getAlertMessage() ),
                new Alert( inputData[6], amountCriteria.getAlertMessage() )
        };

        for( Transaction currInputData : inputData ) {
            fraudDetectorService.process(currInputData);
        }

        for( Alert currAlert : expectedAlerts ) {
            verify(fraudAlertService).alert(currAlert);
        }
    }
}
