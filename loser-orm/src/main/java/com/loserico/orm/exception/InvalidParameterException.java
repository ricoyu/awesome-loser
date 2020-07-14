package com.loserico.orm.exception;

public class InvalidParameterException extends RuntimeException {
	private static final long serialVersionUID = 3055447027521367004L;

	public InvalidParameterException(String message) {
		super(message);
	}
	
	public InvalidParameterException() {
		super();
	}

}
