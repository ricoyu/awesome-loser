package com.loserico.mongo.exception;

/**
 * 反射创建对象实例出错时抛出该异常
 * <p>
 * Copyright: (C), 2020-12-04 17:42
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InstanceCreationException extends RuntimeException {
	
	public InstanceCreationException() {
	}
	
	public InstanceCreationException(String message) {
		super(message);
	}
	
	public InstanceCreationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InstanceCreationException(Throwable cause) {
		super(cause);
	}
	
	public InstanceCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
