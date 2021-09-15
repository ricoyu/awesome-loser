package com.loserico.common.lang.exception;

/**
 * <p>
 * Copyright: (C), 2021-08-27 13:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnsupportedSizeUnitException extends RuntimeException {
	
	public UnsupportedSizeUnitException() {
	}
	
	public UnsupportedSizeUnitException(String message) {
		super(message);
	}
	
	public UnsupportedSizeUnitException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnsupportedSizeUnitException(Throwable cause) {
		super(cause);
	}
	
	public UnsupportedSizeUnitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
