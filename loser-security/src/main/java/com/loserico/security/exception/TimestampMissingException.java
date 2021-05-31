package com.loserico.security.exception;

/**
 * 缺少timestamp参数时抛出该异常
 * <p>
 * Copyright: (C), 2021-05-14 14:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TimestampMissingException extends RuntimeException {
	
	public TimestampMissingException() {
	}
	
	public TimestampMissingException(String message) {
		super(message);
	}
	
	public TimestampMissingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TimestampMissingException(Throwable cause) {
		super(cause);
	}
	
	public TimestampMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
