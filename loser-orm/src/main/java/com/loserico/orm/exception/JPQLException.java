package com.loserico.orm.exception;

public class JPQLException extends RuntimeException {

	private static final long serialVersionUID = 348417769878904415L;

	public JPQLException() {
		super();
	}

	public JPQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JPQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public JPQLException(String message) {
		super(message);
	}

	public JPQLException(Throwable cause) {
		super(cause);
	}

}
