package com.loserico.workbook.exception;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SheetIndexInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1979711868433685408L;

	public SheetIndexInvalidException() {
	}

	public SheetIndexInvalidException(String message) {
		super(message);
	}

	public SheetIndexInvalidException(Throwable cause) {
		super(cause);
	}

	public SheetIndexInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public SheetIndexInvalidException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
