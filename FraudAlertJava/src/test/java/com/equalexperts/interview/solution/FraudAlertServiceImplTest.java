package com.equalexperts.interview.solution;

import com.equalexperts.interview.Alert;
import com.equalexperts.interview.FraudAlertService;
import com.equalexperts.interview.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;


public class FraudAlertServiceImplTest {
	
	@Mock PrintStream outputStream;
	
	FraudAlertService fraudAlertService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		fraudAlertService = new FraudAlertServiceImpl(outputStream);
	}
	
	@Test
	public void testIsPrinting() {
		fraudAlertService.alert( new Alert(new Transaction("ccNumber", new Date(), new BigDecimal(100) ), "message"));
		
		Mockito.verify( outputStream ).println( Mockito.anyString() );
	}
	
	@Test
	public void testPrintingFormat() {
		Alert alert = new Alert(new Transaction("ccNumber", new Date(), new BigDecimal(100) ), "message");
		fraudAlertService.alert( alert);
		
		String expectedOutput = "" +  
			DateFormat.getDateInstance(DateFormat.FULL).format(alert.getTransaction().getTimestamp()) + " " +
			DateFormat.getTimeInstance(DateFormat.FULL).format(alert.getTransaction().getTimestamp()) + " " +
			alert.getTransaction().getCreditCardNumber() + " " +
			alert.getMessage();
		
		Mockito.verify( outputStream ).println( Mockito.eq(expectedOutput));
	}
}
