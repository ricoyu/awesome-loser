package com.loserico.searchlegacy.exception;

/**
 * 创建/更新文档出错时抛出该异常
 * <p>
 * Copyright: (C), 2020-12-03 21:16
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DocumentSaveException extends RuntimeException {
	
	public DocumentSaveException() {
	}
	
	public DocumentSaveException(String message) {
		super(message);
	}
	
	public DocumentSaveException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DocumentSaveException(Throwable cause) {
		super(cause);
	}
	
	public DocumentSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
