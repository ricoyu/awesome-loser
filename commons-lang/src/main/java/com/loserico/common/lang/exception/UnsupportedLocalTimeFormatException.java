package com.loserico.common.lang.exception;

/**
 * 日期字符串不可识别的时候抛出该异常
 * <p>
 * Copyright: (C), 2020-09-25 17:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnsupportedLocalTimeFormatException extends RuntimeException{
	
	public UnsupportedLocalTimeFormatException() {
	}
	
	public UnsupportedLocalTimeFormatException(String message) {
		super(message);
	}
	
	public UnsupportedLocalTimeFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnsupportedLocalTimeFormatException(Throwable cause) {
		super(cause);
	}
	
	public UnsupportedLocalTimeFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
