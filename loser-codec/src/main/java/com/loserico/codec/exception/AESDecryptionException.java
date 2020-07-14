package com.loserico.codec.exception;

/**
 * AES 解密时抛出异常
 * <p>
 * Copyright: Copyright (c) 2018-08-20 17:09
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class AESDecryptionException extends RuntimeException{

	private static final long serialVersionUID = 1178059898844962694L;

	public AESDecryptionException() {
		super();
	}

	public AESDecryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AESDecryptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AESDecryptionException(String message) {
		super(message);
	}

	public AESDecryptionException(Throwable cause) {
		super(cause);
	}

	
}
