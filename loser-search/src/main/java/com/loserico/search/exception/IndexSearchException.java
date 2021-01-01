package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-30 8:40
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexSearchException extends RuntimeException {
	
	public IndexSearchException() {
	}
	
	public IndexSearchException(String message) {
		super(message);
	}
	
	public IndexSearchException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IndexSearchException(Throwable cause) {
		super(cause);
	}
	
	public IndexSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
