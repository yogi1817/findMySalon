package com.spj.salon.exception;

import java.util.Date;

/**
 * 
 * @author Yogesh Sharma
 *
 */
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;
	private String uuid;

	public ErrorDetails(Date timestamp, String message, String details,String uuid) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.uuid = uuid;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return this.uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}