package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2021-02-14 10:32
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SuggestException extends RuntimeException {
	
	public SuggestException() {
	}
	
	public SuggestException(String message) {
		super(message);
	}
	
	public SuggestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SuggestException(Throwable cause) {
		super(cause);
	}
	
	public SuggestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
