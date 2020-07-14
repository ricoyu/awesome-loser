package com.loserico.common.lang.exception;

/**
 * 多线程环境下异步执行时遇异常则抛出
 * <p>
 * Copyright: Copyright (c) 2018-09-14 15:08
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class AsyncExecutionException extends RuntimeException {

	private static final long serialVersionUID = -8117593274171841094L;

	public AsyncExecutionException() {
	}

	public AsyncExecutionException(String message) {
		super(message);
	}

	public AsyncExecutionException(Throwable cause) {
		super(cause);
	}

	public AsyncExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AsyncExecutionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}