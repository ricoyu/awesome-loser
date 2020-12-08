package com.loserico.codec.exception;

/**
 * RSA 私钥签名出错时抛出的异常
 * <p>
 * Copyright: (C), 2020-12-02 11:53
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaSignException extends RuntimeException {
	
	public RsaSignException() {
	}
	
	public RsaSignException(String message) {
		super(message);
	}
	
	public RsaSignException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RsaSignException(Throwable cause) {
		super(cause);
	}
	
	public RsaSignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
