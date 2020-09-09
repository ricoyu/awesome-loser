package com.loserico.codec.exception;

/**
 * 获取RSA私钥对象出错时抛出本异常
 * <p>
 * Copyright: (C), 2020/5/22 18:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaPrivateKeyException extends RuntimeException {
	
	public RsaPrivateKeyException() {
		super();
	}
	
	public RsaPrivateKeyException(String message) {
		super(message);
	}
	
	public RsaPrivateKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RsaPrivateKeyException(Throwable cause) {
		super(cause);
	}
	
	protected RsaPrivateKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
