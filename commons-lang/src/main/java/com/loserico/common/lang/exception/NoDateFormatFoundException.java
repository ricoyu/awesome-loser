package com.loserico.common.lang.exception;

/**
 * 日期字符串找不到对应的SimpleDateFormatter时抛出本异常
 * <p>
 * Copyright: (C), 2020-09-25 17:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NoDateFormatFoundException extends RuntimeException {
	
	public NoDateFormatFoundException() {
	}
	
	public NoDateFormatFoundException(String message) {
		super(message);
	}
	
	public NoDateFormatFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoDateFormatFoundException(Throwable cause) {
		super(cause);
	}
	
	public NoDateFormatFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
