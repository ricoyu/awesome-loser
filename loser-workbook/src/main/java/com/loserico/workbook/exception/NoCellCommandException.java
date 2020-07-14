package com.loserico.workbook.exception;

/**
 * 缺少对应数据类型的CellCommand时抛出该异常 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class NoCellCommandException extends RuntimeException {

	public NoCellCommandException() {
	}

	public NoCellCommandException(String message) {
		super(message);
	}

	public NoCellCommandException(Throwable cause) {
		super(cause);
	}

	public NoCellCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCellCommandException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
