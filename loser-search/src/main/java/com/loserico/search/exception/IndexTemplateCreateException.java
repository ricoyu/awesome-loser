package com.loserico.search.exception;

/**
 * 创建 Index Template失败时抛出该异常
 * <p>
 * Copyright: (C), 2021-06-30 11:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexTemplateCreateException extends RuntimeException {
	
	public IndexTemplateCreateException() {
	}
	
	public IndexTemplateCreateException(String message) {
		super(message);
	}
	
	public IndexTemplateCreateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IndexTemplateCreateException(Throwable cause) {
		super(cause);
	}
	
	public IndexTemplateCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
