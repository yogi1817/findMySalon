package com.spj.salon.exception;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Data
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

	public ErrorDetails() {
		
	}
}