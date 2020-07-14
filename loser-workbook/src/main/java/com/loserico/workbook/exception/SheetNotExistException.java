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
public class SheetNotExistException extends RuntimeException {

	private static final long serialVersionUID = -4127120873449254565L;

	public SheetNotExistException() {
	}

	public SheetNotExistException(String message) {
		super(message);
	}

	public SheetNotExistException(Throwable cause) {
		super(cause);
	}

	public SheetNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public SheetNotExistException(String message, Throwable cause, boolean enableSuppression,
	                              boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
