package com.loserico.common.lang.exception;

/**
 * 找不到实体对象, 但是确信应该在的时候抛出该异常
 * <p>
 * Copyright: (C), 2020-08-20 14:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EntityNotFoundException extends RuntimeException{
	
	public EntityNotFoundException() {
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}
	
	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
