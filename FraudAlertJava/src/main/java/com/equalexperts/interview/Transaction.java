package com.equalexperts.interview;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Transaction object to store transaction details.
 * 
 * @author Expedia, Inc.
 */
public class Transaction {
	private String creditCardNumber;
	private Date timestamp;
	private BigDecimal amount;
	
	public Transaction(String creditCardNumber, Date timestamp, BigDecimal amount) {
		this.creditCardNumber = creditCardNumber;
		this.timestamp = timestamp;
		this.amount = amount;
	}
	
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount.hashCode();
		result = prime
				* result
				+ ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (!amount.equals(other.amount))
			return false;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [creditCardNumber=" + creditCardNumber
				+ ", timestamp=" + timestamp.getTime() + ", amount=" + amount + "]";
	}
}
