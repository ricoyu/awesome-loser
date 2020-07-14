package com.loserico.codec.exception;

/**
 * <p>
 * Copyright: (C), 2020/5/21 15:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HmacSha256Exception extends RuntimeException {
	
	public HmacSha256Exception() {
		super();
	}
	
	public HmacSha256Exception(String message) {
		super(message);
	}
	
	public HmacSha256Exception(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HmacSha256Exception(Throwable cause) {
		super(cause);
	}
	
	protected HmacSha256Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
