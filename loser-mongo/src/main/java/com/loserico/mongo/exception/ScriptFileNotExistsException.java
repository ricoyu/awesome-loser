package com.loserico.mongo.exception;

/**
 * <p>
 * Copyright: (C), 2020-09-23 13:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ScriptFileNotExistsException extends RuntimeException {
	
	public ScriptFileNotExistsException() {
	}
	
	public ScriptFileNotExistsException(String message) {
		super(message);
	}
	
	public ScriptFileNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ScriptFileNotExistsException(Throwable cause) {
		super(cause);
	}
	
	public ScriptFileNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
