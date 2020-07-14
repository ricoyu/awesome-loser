package com.loserico.cache.exception;

/**
 * KEY不合法时抛出的异常
 * <p>
 * Copyright: (C), 2020/3/31 15:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InvalidKeyException extends RuntimeException {
	
	public InvalidKeyException() {
		super();
	}
	
	public InvalidKeyException(String message) {
		super(message);
	}
	
	public InvalidKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidKeyException(Throwable cause) {
		super(cause);
	}
	
	protected InvalidKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
