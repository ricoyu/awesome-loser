package com.loserico.common.lang.exception;

/**
 * <p>
 * Copyright: (C), 2019/12/29 19:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IORuntimeException extends RuntimeException{
	
	public IORuntimeException() {
	}
	
	public IORuntimeException(String message) {
		super(message);
	}
	
	public IORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IORuntimeException(Throwable cause) {
		super(cause);
	}
	
	protected IORuntimeException(String message, Throwable cause, boolean enableSuppression,
	                             boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
