package com.loserico.workbook.exception;

/**
 * 在Excel中找不到指定的Row时抛出 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RowNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2642455727967639543L;

	public RowNotFoundException() {
	}

	public RowNotFoundException(String message) {
		super(message);
	}

	public RowNotFoundException(Throwable cause) {
		super(cause);
	}

	public RowNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RowNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
