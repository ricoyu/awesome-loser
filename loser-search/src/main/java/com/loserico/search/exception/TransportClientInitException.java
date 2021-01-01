package com.loserico.search.exception;

/**
 * 初始化TransportClient失败时抛出该异常
 * <p>
 * Copyright: (C), 2021-01-01 8:59
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TransportClientInitException extends RuntimeException {
	
	public TransportClientInitException() {
	}
	
	public TransportClientInitException(String message) {
		super(message);
	}
	
	public TransportClientInitException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TransportClientInitException(Throwable cause) {
		super(cause);
	}
	
	public TransportClientInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
