package com.loserico.codec.exception;

/**
 * 实例化Cipher时抛出的异常
 * <p>
 * Copyright: Copyright (c) 2018-08-20 17:04
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class CipherInitializeException extends RuntimeException {

	private static final long serialVersionUID = -2280441644391363490L;

	public CipherInitializeException() {
		super();
	}

	public CipherInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CipherInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CipherInitializeException(String message) {
		super(message);
	}

	public CipherInitializeException(Throwable cause) {
		super(cause);
	}

}
