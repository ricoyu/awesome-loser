package com.loserico.workbook.exception;

/**
 * Excel中找不到指定的Cell时抛出 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:32
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CellNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1328621141471121771L;

	public CellNotFoundException() {
	}

	public CellNotFoundException(String message) {
		super(message);
	}

	public CellNotFoundException(Throwable cause) {
		super(cause);
	}

	public CellNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CellNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
