package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-26 9:36
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MappingException extends RuntimeException {
	
	public MappingException() {
	}
	
	public MappingException(String message) {
		super(message);
	}
	
	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MappingException(Throwable cause) {
		super(cause);
	}
	
	public MappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
