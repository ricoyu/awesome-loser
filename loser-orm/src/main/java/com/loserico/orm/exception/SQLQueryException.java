package com.loserico.orm.exception;

public class SQLQueryException extends RuntimeException {

	private static final long serialVersionUID = -8568284300374008131L;

	public SQLQueryException() {
		super();
	}

	public SQLQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SQLQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public SQLQueryException(String message) {
		super(message);
	}

	public SQLQueryException(Throwable cause) {
		super(cause);
	}

}
