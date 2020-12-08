package com.loserico.search.exception;

/**
 * 更新文档出错时抛出该异常
 * <p>
 * Copyright: (C), 2020-12-06 14:23
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DocumentUpdateException extends RuntimeException {
	
	public DocumentUpdateException() {
	}
	
	public DocumentUpdateException(String message) {
		super(message);
	}
	
	public DocumentUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DocumentUpdateException(Throwable cause) {
		super(cause);
	}
	
	public DocumentUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
