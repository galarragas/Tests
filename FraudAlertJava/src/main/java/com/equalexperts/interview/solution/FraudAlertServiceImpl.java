package com.equalexperts.interview.solution;

import com.equalexperts.interview.Alert;
import com.equalexperts.interview.FraudAlertService;

import java.io.PrintStream;
import java.text.MessageFormat;

public class FraudAlertServiceImpl implements FraudAlertService {

	private static final String messageFormat = "{0,date,full} {0,time,full} {1} {2}";
	private PrintStream outputStream;

	public FraudAlertServiceImpl() {
		this( System.out );
	}
	
	FraudAlertServiceImpl( PrintStream outputStream ) {
		this.outputStream = outputStream;
	}
	
	// prints current timestamp, credit card number and message from the Alert
	// object separated by space
	public void alert(Alert alert) {
		outputStream.println(MessageFormat.format(messageFormat, alert
				.getTransaction().getTimestamp(), alert.getTransaction()
				.getCreditCardNumber(), alert.getMessage()
			)
		);
	}

}
