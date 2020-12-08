package com.loserico.search.exception;

/**
 * 检查索引存在性时出错则抛出这个异常
 * <p>
 * Copyright: (C), 2020-12-06 12:52
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexExistsException extends RuntimeException {
	
	public IndexExistsException() {
	}
	
	public IndexExistsException(String message) {
		super(message);
	}
	
	public IndexExistsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IndexExistsException(Throwable cause) {
		super(cause);
	}
	
	public IndexExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
