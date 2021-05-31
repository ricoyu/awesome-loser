package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2021-05-06 15:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InvalidSettingsException extends RuntimeException {
	
	public InvalidSettingsException() {
	}
	
	public InvalidSettingsException(String message) {
		super(message);
	}
	
	public InvalidSettingsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidSettingsException(Throwable cause) {
		super(cause);
	}
	
	public InvalidSettingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
