package com.loserico.common.lang.exception;

import com.loserico.common.lang.vo.ErrorType;

/**
 * 通用业务异常
 * <p>
 * Copyright: (C), 2020/5/17 19:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BusinessException extends RuntimeException {
	
	private String code;
	
	private String message;
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(ErrorType errorType) {
		super(errorType.getMsg());
		this.code = errorType.getCode();
		this.message = errorType.getMsg();
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
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
