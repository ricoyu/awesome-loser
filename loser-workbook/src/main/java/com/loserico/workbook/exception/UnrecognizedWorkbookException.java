package com.loserico.workbook.exception;

/**
 * 给定的Workbook格式不能被识别时抛出 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class UnrecognizedWorkbookException extends RuntimeException {

	private static final long serialVersionUID = -7019678665057695863L;

	public UnrecognizedWorkbookException() {
		super();
	}

	public UnrecognizedWorkbookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnrecognizedWorkbookException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnrecognizedWorkbookException(String message) {
		super(message);
	}

	public UnrecognizedWorkbookException(Throwable cause) {
		super(cause);
	}

}
