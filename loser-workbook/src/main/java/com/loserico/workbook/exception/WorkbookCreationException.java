package com.loserico.workbook.exception;

/**
 * 创建Excel文件失败时抛出该异常 
 * <p>
 * Copyright: Copyright (c) 2019-10-15 10:33
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class WorkbookCreationException extends RuntimeException {

	private static final long serialVersionUID = 1371909471638398673L;

	public WorkbookCreationException() {
	}

	public WorkbookCreationException(String message) {
		super(message);
	}

	public WorkbookCreationException(Throwable cause) {
		super(cause);
	}

	public WorkbookCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkbookCreationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}