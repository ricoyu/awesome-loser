package com.loserico.security.exception;

/**
 * timestamp参数不是合法的毫秒数时抛出该异常
 * <p>
 * Copyright: (C), 2021-05-14 14:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TimestampInvalidException extends RuntimeException {
	
	public TimestampInvalidException() {
	}
	
	public TimestampInvalidException(String message) {
		super(message);
	}
	
	public TimestampInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TimestampInvalidException(Throwable cause) {
		super(cause);
	}
	
	public TimestampInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
