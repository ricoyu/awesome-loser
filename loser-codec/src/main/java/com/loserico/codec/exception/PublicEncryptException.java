package com.loserico.codec.exception;

/**
 * 公钥加密失败时抛出的异常
 * <p>
 * Copyright: (C), 2020-12-02 11:51
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PublicEncryptException extends RuntimeException {
	
	public PublicEncryptException() {
	}
	
	public PublicEncryptException(String message) {
		super(message);
	}
	
	public PublicEncryptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PublicEncryptException(Throwable cause) {
		super(cause);
	}
	
	public PublicEncryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
