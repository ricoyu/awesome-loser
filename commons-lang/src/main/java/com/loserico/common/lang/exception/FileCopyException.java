package com.loserico.common.lang.exception;

/**
 * 文件拷贝失败时抛出异常
 * <p>
 * Copyright: Copyright (c) 2018-06-11 14:47
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 */
public class FileCopyException extends RuntimeException {

	private static final long serialVersionUID = -6583619997292063693L;

	public FileCopyException() {
		super();
	}

	public FileCopyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileCopyException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileCopyException(String message) {
		super(message);
	}

	public FileCopyException(Throwable cause) {
		super(cause);
	}

}
