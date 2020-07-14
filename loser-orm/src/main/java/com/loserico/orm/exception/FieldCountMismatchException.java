package com.loserico.orm.exception;

/**
 * 查询的SQL返回记录的field数量与bean中属性数量不一致时抛出该异常
 * @author Rico Yu	ricoyu520@gmail.com
 * @since 2017-03-13 13:48
 * @version 1.0
 *
 */
public class FieldCountMismatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FieldCountMismatchException() {
	}

	public FieldCountMismatchException(String message) {
		super(message);
	}

	public FieldCountMismatchException(Throwable cause) {
		super(cause);
	}

	public FieldCountMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldCountMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
