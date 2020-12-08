package com.loserico.codec.exception;

/**
 * 私钥加密失败时抛出的异常
 * <p>
 * Copyright: (C), 2020-12-02 11:50
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PrivateEncryptException extends RuntimeException {
	
	public PrivateEncryptException() {
	}
	
	public PrivateEncryptException(String message) {
		super(message);
	}
	
	public PrivateEncryptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PrivateEncryptException(Throwable cause) {
		super(cause);
	}
	
	public PrivateEncryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
