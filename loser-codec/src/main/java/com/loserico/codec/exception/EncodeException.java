package com.loserico.codec.exception;

/**
 * <p>
 * Copyright: (C), 2019/10/20 15:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @version 1.0
 * @author  Rico Yu ricoyu520@gmail.com
 */
public class EncodeException extends RuntimeException {
	public EncodeException() {
		super();
	}
	
	public EncodeException(String message) {
		super(message);
	}
	
	public EncodeException(Throwable cause) {
		super(cause);
	}
	
	protected EncodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
