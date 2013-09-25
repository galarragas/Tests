package com.equalexperts.interview;

/**
 * Alert that can be submitted to the AlertService
 * 
 * @author Expedia, Inc.
 */
public class Alert {

	/**
	 * Referenced transaction
	 */
	private Transaction transaction;
	
	/**
	 * Message
	 */
	private String message;

	/**
	 * Creates new instance of <code>Alert</code>
	 */
	public Alert(Transaction transaction, String message) {
		this.transaction = transaction;
		this.message = message;
	}

	/**
     * @return the transaction
     */
    public Transaction getTransaction() {
    	return transaction;
    }

	/**
     * @return the message
     */
    public String getMessage() {
    	return message;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((transaction == null) ? 0 : transaction.hashCode());
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
		Alert other = (Alert) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Alert [transaction=" + transaction + ", message=" + message
				+ "]";
	}
    
    
}
