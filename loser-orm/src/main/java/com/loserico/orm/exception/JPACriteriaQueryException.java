package com.loserico.orm.exception;

public class JPACriteriaQueryException extends RuntimeException {

	private static final long serialVersionUID = -5810160664107922453L;

	public JPACriteriaQueryException() {
		super();
	}

	public JPACriteriaQueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JPACriteriaQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public JPACriteriaQueryException(String message) {
		super(message);
	}

	public JPACriteriaQueryException(Throwable cause) {
		super(cause);
	}

}
