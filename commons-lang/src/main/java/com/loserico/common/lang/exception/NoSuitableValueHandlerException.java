package com.loserico.common.lang.exception;

/**
 * 找不到对应的类型转换器时抛出该异常
 * <p>
 * Copyright: (C), 2020-12-05 15:46
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NoSuitableValueHandlerException extends RuntimeException {
	
	public NoSuitableValueHandlerException() {
	}
	
	public NoSuitableValueHandlerException(String message) {
		super(message);
	}
	
	public NoSuitableValueHandlerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuitableValueHandlerException(Throwable cause) {
		super(cause);
	}
	
	public NoSuitableValueHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
