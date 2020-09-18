package com.loserico.json.exception;

/**
 * Jackson操作统一异常
 * <p>
 * Copyright: (C), 2020-09-17 17:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonException extends RuntimeException {
	
	public JacksonException() {
	}
	
	public JacksonException(String message) {
		super(message);
	}
	
	public JacksonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JacksonException(Throwable cause) {
		super(cause);
	}
	
	public JacksonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
