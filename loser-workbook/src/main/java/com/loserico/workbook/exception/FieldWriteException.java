package com.loserico.workbook.exception;

/**
 * 通过反射给Field设置值的时候, 如果出错则出该的异常 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:32
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class FieldWriteException extends RuntimeException {

	public FieldWriteException() {
	}

	public FieldWriteException(String message) {
		super(message);
	}

	public FieldWriteException(Throwable cause) {
		super(cause);
	}

	public FieldWriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
