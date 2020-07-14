package com.loserico.orm.exception;

public class PropertyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8764128861224578000L;

	public PropertyNotFoundException() {
	}

	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}

	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
