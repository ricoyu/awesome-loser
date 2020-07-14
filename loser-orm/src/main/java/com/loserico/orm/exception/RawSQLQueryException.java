package com.loserico.orm.exception;

public class RawSQLQueryException extends RuntimeException {

	private static final long serialVersionUID = -782276723683337717L;

	public RawSQLQueryException() {
		super();
	}

	public RawSQLQueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RawSQLQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RawSQLQueryException(String message) {
		super(message);
	}

	public RawSQLQueryException(Throwable cause) {
		super(cause);
	}

}
