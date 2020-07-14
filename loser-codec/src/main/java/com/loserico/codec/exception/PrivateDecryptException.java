package com.loserico.codec.exception;

/**
 * 私钥解密时出错
 * <p>
 * Copyright: Copyright (c) 2018-08-02 13:25
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class PrivateDecryptException extends RuntimeException {

	private static final long serialVersionUID = -4976088177578942984L;

	public PrivateDecryptException() {
		super();
	}

	public PrivateDecryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrivateDecryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrivateDecryptException(String message) {
		super(message);
	}

	public PrivateDecryptException(Throwable cause) {
		super(cause);
	}

}
