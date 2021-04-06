package com.loserico.security.exception;

/**
 * 用户Token已过期
 * <p>
 * Copyright: (C), 2021-04-01 10:45
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TokenExpiredException extends RuntimeException {
	
	public TokenExpiredException() {
	}
	
	public TokenExpiredException(String message) {
		super(message);
	}
	
	public TokenExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TokenExpiredException(Throwable cause) {
		super(cause);
	}
	
	public TokenExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
