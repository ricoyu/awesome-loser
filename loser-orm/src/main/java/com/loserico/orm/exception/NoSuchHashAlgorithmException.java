package com.loserico.orm.exception;

public class NoSuchHashAlgorithmException extends RuntimeException {

	private static final long serialVersionUID = 1606042461767033219L;

	public NoSuchHashAlgorithmException() {
		super();
	}

	public NoSuchHashAlgorithmException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchHashAlgorithmException(String msg) {
		super(msg);
	}

	public NoSuchHashAlgorithmException(Throwable cause) {
		super(cause);
	}

}
