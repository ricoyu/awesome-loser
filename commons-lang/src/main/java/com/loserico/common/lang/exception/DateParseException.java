package com.loserico.common.lang.exception;

/**
 * 解析日期字符串到Date对象失败时抛出本异常
 * <p>
 * Copyright: (C), 2020-09-25 17:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DateParseException extends RuntimeException {
	
	public DateParseException() {
	}
	
	public DateParseException(String message) {
		super(message);
	}
	
	public DateParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DateParseException(Throwable cause) {
		super(cause);
	}
	
	public DateParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
