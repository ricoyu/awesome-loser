package com.loserico.mongo.exception;

/**
 * <p>
 * Copyright: (C), 2020-09-23 11:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InvalidScriptException extends RuntimeException {
	
	public InvalidScriptException() {
	}
	
	public InvalidScriptException(String message) {
		super(message);
	}
	
	public InvalidScriptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidScriptException(Throwable cause) {
		super(cause);
	}
	
	public InvalidScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
