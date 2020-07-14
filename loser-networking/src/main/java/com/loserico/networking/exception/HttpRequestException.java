package com.loserico.networking.exception;

/**
 * <p>
 * Copyright: (C), 2019/12/25 11:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HttpRequestException extends RuntimeException {
	
	public HttpRequestException() {
	}
	
	public HttpRequestException(String message) {
		super(message);
	}
	
	public HttpRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HttpRequestException(Throwable cause) {
		super(cause);
	}
	
	protected HttpRequestException(String message, Throwable cause, boolean enableSuppression,
	                               boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
