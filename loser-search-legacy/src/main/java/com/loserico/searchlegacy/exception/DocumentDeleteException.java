package com.loserico.searchlegacy.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-06 19:49
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DocumentDeleteException extends RuntimeException {
	
	public DocumentDeleteException() {
	}
	
	public DocumentDeleteException(String message) {
		super(message);
	}
	
	public DocumentDeleteException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DocumentDeleteException(Throwable cause) {
		super(cause);
	}
	
	public DocumentDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
