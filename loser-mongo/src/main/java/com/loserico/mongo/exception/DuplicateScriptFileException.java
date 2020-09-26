package com.loserico.mongo.exception;

/**
 * <p>
 * Copyright: (C), 2020-09-23 10:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DuplicateScriptFileException extends RuntimeException {
	
	public DuplicateScriptFileException() {
	}
	
	public DuplicateScriptFileException(String message) {
		super(message);
	}
	
	public DuplicateScriptFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DuplicateScriptFileException(Throwable cause) {
		super(cause);
	}
	
	public DuplicateScriptFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
