package com.loserico.common.lang.exception;

/**
 * 解析日期字符串到LocalDateTime对象失败时抛出本异常
 * <p>
 * Copyright: (C), 2020-09-25 17:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateTimeException extends RuntimeException{
	
	public LocalDateTimeException() {
	}
	
	public LocalDateTimeException(String message) {
		super(message);
	}
	
	public LocalDateTimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LocalDateTimeException(Throwable cause) {
		super(cause);
	}
	
	public LocalDateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
