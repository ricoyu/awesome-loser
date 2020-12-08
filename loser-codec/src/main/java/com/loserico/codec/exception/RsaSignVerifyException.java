package com.loserico.codec.exception;

/**
 * RSA 校验签名失败时抛出的异常
 * <p>
 * Copyright: (C), 2020-12-02 11:55
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaSignVerifyException extends RuntimeException {
	
	public RsaSignVerifyException() {
	}
	
	public RsaSignVerifyException(String message) {
		super(message);
	}
	
	public RsaSignVerifyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RsaSignVerifyException(Throwable cause) {
		super(cause);
	}
	
	public RsaSignVerifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
