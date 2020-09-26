package com.loserico.common.lang.exception;

/**
 * 解析日期字符串到LocalDate对象失败时抛出本异常
 * <p>
 * Copyright: (C), 2020-09-25 17:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateParseException extends RuntimeException {
	
	public LocalDateParseException() {
	}
	
	public LocalDateParseException(String message) {
		super(message);
	}
	
	public LocalDateParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LocalDateParseException(Throwable cause) {
		super(cause);
	}
	
	public LocalDateParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
