package com.loserico.searchlegacy.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-06 19:38
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DocumentExistsException extends RuntimeException {
	
	public DocumentExistsException() {
	}
	
	public DocumentExistsException(String message) {
		super(message);
	}
	
	public DocumentExistsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DocumentExistsException(Throwable cause) {
		super(cause);
	}
	
	public DocumentExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
