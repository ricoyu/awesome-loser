package com.loserico.orm.exception;

public class SQLCountQueryException extends RuntimeException {

	private static final long serialVersionUID = -4678768848981535755L;

	public SQLCountQueryException() {
		super();
	}

	public SQLCountQueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SQLCountQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLCountQueryException(String message) {
		super(message);
	}

	public SQLCountQueryException(Throwable cause) {
		super(cause);
	}

}
