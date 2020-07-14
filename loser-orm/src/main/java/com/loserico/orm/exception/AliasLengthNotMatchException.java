package com.loserico.orm.exception;

public class AliasLengthNotMatchException extends RuntimeException {
	private static final long serialVersionUID = 3055447027521367004L;

	public AliasLengthNotMatchException(String message) {
		super(message);
	}
	
	public AliasLengthNotMatchException() {
		super();
	}

}
