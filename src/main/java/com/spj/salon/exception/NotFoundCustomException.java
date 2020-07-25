package com.spj.salon.exception;

public class NotFoundCustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1256873401088084366L;
	
	private String message;
	private String details;
	
	public NotFoundCustomException(String message, String details) {
		super();
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}
}
