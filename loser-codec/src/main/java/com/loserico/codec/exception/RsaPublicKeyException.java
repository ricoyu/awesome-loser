package com.loserico.codec.exception;

/**
 * 获取RSA公钥对象出错是抛出本异常
 * <p>
 * Copyright: (C), 2020/5/22 18:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaPublicKeyException extends RuntimeException {
	
	public RsaPublicKeyException() {
		super();
	}
	
	public RsaPublicKeyException(String message) {
		super(message);
	}
	
	public RsaPublicKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RsaPublicKeyException(Throwable cause) {
		super(cause);
	}
	
	protected RsaPublicKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
