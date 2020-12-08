package com.loserico.codec.exception;

/**
 * 公钥解密失败时抛出的异常
 * <p>
 * Copyright: (C), 2020-12-02 11:52
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PublicDecryptException extends RuntimeException {
	
	public PublicDecryptException() {
	}
	
	public PublicDecryptException(String message) {
		super(message);
	}
	
	public PublicDecryptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PublicDecryptException(Throwable cause) {
		super(cause);
	}
	
	public PublicDecryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
