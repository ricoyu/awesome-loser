package com.loserico.networking.exception;

/**
 * {@link InvalidHttpTypeException}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2021/7/8
 */
public class InvalidHttpTypeException extends RuntimeException {

	private static final long serialVersionUID = 7980095122593887404L;

	public InvalidHttpTypeException() {
	}

	public InvalidHttpTypeException(String message) {
		super(message);
	}

	public InvalidHttpTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidHttpTypeException(Throwable cause) {
		super(cause);
	}

	protected InvalidHttpTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
