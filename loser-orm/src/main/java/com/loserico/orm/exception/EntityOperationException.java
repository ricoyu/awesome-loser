package com.loserico.orm.exception;

public class EntityOperationException extends RuntimeException {

	private static final long serialVersionUID = -8977702325095175531L;

	public EntityOperationException() {
		super();
	}

	public EntityOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityOperationException(String message) {
		super(message);
	}

	public EntityOperationException(Throwable cause) {
		super(cause);
	}

}
