package com.loserico.searchlegacy.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-24 9:10
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DocumentGetException extends RuntimeException {
	
	public DocumentGetException() {
	}
	
	public DocumentGetException(String message) {
		super(message);
	}
	
	public DocumentGetException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DocumentGetException(Throwable cause) {
		super(cause);
	}
	
	public DocumentGetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
