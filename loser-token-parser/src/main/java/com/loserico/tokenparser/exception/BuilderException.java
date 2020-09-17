package com.loserico.tokenparser.exception;

/**
 * <p>
 * Copyright: (C), 2020-09-16 10:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BuilderException extends RuntimeException {
	
	public BuilderException() {
	}
	
	public BuilderException(String message) {
		super(message);
	}
	
	public BuilderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BuilderException(Throwable cause) {
		super(cause);
	}
	
	public BuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
