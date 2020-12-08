package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-06 20:00
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BulkOperationException extends RuntimeException {
	
	public BulkOperationException() {
	}
	
	public BulkOperationException(String message) {
		super(message);
	}
	
	public BulkOperationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BulkOperationException(Throwable cause) {
		super(cause);
	}
	
	public BulkOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
