package com.loserico.cache.exception;

/**
 * 在操作Redis的Hash value时抛出异常
 * <p>
 * Copyright: Copyright (c) 2018-09-13 11:45
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class JedisValueOperationException extends RuntimeException {

	private static final long serialVersionUID = 2394373872615726786L;

	public JedisValueOperationException() {
	}

	public JedisValueOperationException(String message) {
		super(message);
	}

	public JedisValueOperationException(Throwable cause) {
		super(cause);
	}

	public JedisValueOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JedisValueOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
