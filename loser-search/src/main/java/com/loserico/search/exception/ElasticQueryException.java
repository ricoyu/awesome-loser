package com.loserico.search.exception;

/**
 * 通用查询异常
 * <p>
 * Copyright: (C), 2021-01-12 9:07
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticQueryException extends RuntimeException {
	
	public ElasticQueryException() {
	}
	
	public ElasticQueryException(String message) {
		super(message);
	}
	
	public ElasticQueryException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ElasticQueryException(Throwable cause) {
		super(cause);
	}
	
	public ElasticQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
