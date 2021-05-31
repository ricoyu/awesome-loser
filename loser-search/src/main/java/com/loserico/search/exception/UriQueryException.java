package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2021-04-29 17:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UriQueryException extends RuntimeException {
	
	public UriQueryException() {
	}
	
	public UriQueryException(String message) {
		super(message);
	}
	
	public UriQueryException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UriQueryException(Throwable cause) {
		super(cause);
	}
	
	public UriQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
