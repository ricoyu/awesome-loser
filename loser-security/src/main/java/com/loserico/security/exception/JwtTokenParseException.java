package com.loserico.security.exception;

/**
 * 根据String token解析Jwt失败时抛出该异常
 * <p>
 * Copyright: (C), 2020/5/23 16:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JwtTokenParseException extends RuntimeException {
	
	public JwtTokenParseException() {
		super();
	}
	
	public JwtTokenParseException(String message) {
		super(message);
	}
	
	public JwtTokenParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JwtTokenParseException(Throwable cause) {
		super(cause);
	}
	
	protected JwtTokenParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
