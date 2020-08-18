package com.spj.salon.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
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

	public NotFoundCustomException() {
		
	}
}
