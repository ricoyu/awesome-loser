package com.loserico.common.lang.exception;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:51
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ConcurrentOperationException extends RuntimeException {

	private static final long serialVersionUID = 5008555376612220264L;

	public ConcurrentOperationException() {
	}

	public ConcurrentOperationException(String message) {
		super(message);
	}

	public ConcurrentOperationException(Throwable cause) {
		super(cause);
	}

	public ConcurrentOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConcurrentOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}