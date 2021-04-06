package com.loserico.common.lang.exception;

/**
 * 错误码不符合规范时抛出该异常
 * <p>
 * Copyright: (C), 2021-03-30 11:08
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InvalidCodeException extends RuntimeException {
	
	public InvalidCodeException() {
	}
	
	public InvalidCodeException(String message) {
		super(message);
	}
	
	public InvalidCodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidCodeException(Throwable cause) {
		super(cause);
	}
	
	public InvalidCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
