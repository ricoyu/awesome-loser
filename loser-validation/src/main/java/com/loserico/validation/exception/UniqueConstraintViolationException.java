package com.loserico.validation.exception;

/**
 * <p>
 * Copyright: (C), 2019/10/21 10:03
 * <p>
 * <p>
 * Company Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UniqueConstraintViolationException extends RuntimeException {
	
	public UniqueConstraintViolationException() {
		super();
	}
	
	public UniqueConstraintViolationException(String message) {
		super(message);
	}
	
	public UniqueConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UniqueConstraintViolationException(Throwable cause) {
		super(cause);
	}
	
	protected UniqueConstraintViolationException(String message, Throwable cause, boolean enableSuppression,
												 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
