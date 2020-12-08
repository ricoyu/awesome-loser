package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-06 19:44
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexDeleteException extends RuntimeException {
	
	public IndexDeleteException() {
	}
	
	public IndexDeleteException(String message) {
		super(message);
	}
	
	public IndexDeleteException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IndexDeleteException(Throwable cause) {
		super(cause);
	}
	
	public IndexDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
