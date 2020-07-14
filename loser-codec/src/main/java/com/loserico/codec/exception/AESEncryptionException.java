package com.loserico.codec.exception;

/**
 * AES 加密失败异常
 * <p>
 * Copyright: Copyright (c) 2018-08-20 17:06
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class AESEncryptionException extends RuntimeException {

	private static final long serialVersionUID = -7574574381366212188L;

	public AESEncryptionException() {
		super();
	}

	public AESEncryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AESEncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AESEncryptionException(String message) {
		super(message);
	}

	public AESEncryptionException(Throwable cause) {
		super(cause);
	}

}
