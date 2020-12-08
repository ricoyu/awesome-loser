package com.loserico.search.exception;

/**
 * 查询索引列表时抛出该异常
 * <p>
 * Copyright: (C), 2020-12-06 13:26
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ListIndicesException extends RuntimeException {
	
	public ListIndicesException() {
	}
	
	public ListIndicesException(String message) {
		super(message);
	}
	
	public ListIndicesException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ListIndicesException(Throwable cause) {
		super(cause);
	}
	
	public ListIndicesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
