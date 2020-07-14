package com.loserico.workbook.exception;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class InvalidVarTemplateException extends RuntimeException {

	private static final long serialVersionUID = -1404780319518180287L;

	public InvalidVarTemplateException() {
	}

	public InvalidVarTemplateException(String message) {
		super(message);
	}

	public InvalidVarTemplateException(Throwable cause) {
		super(cause);
	}

	public InvalidVarTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidVarTemplateException(String message, Throwable cause, boolean enableSuppression,
	                                   boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}