package com.loserico.common.lang.exception;

/**
 * <p>
 * Copyright: (C), 2019/10/31 18:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ValidationException extends RuntimeException {
	
	public static final String ROW_NUM = "rowNum";
	
	public ValidationException() {
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ValidationException(Throwable cause) {
		super(cause);
	}
}