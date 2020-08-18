package com.loserico.common.lang.exception;

import com.loserico.common.lang.vo.ErrorType;

/**
 * <p>
 * Copyright: (C), 2020-08-17 17:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ApplicationException extends RuntimeException {
	
	private String code = "500";
	
	private String message = "Internal Server Error";
	
	public ApplicationException() {
		super();
	}
	
	public ApplicationException(ErrorType errorType) {
		super(errorType.getMsg());
		this.code = errorType.getCode();
		this.message = errorType.getMsg();
	}
	
	public ApplicationException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public ApplicationException(String message) {
		super(message);
		this.message = message;
	}
	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	protected ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
