package com.loserico.workbook.exception;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:32
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BuilderUncompleteException extends RuntimeException {

	private static final long serialVersionUID = 8492104693313185654L;

	public BuilderUncompleteException() {
	}

	public BuilderUncompleteException(String message) {
		super(message);
	}

	public BuilderUncompleteException(Throwable cause) {
		super(cause);
	}

	public BuilderUncompleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuilderUncompleteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
