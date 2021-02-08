package com.loserico.validation.exception;

/**
 * 手工进行数据校验失败时抛出该异常 
 * <p>
 * Copyright: Copyright (c) 2021-02-03 10:33
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ValidationException extends RuntimeException {
	
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
