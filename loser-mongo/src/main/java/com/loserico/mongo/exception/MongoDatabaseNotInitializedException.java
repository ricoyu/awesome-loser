package com.loserico.mongo.exception;

/**
 * <p>
 * Copyright: (C), 2020-12-14 16:18
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MongoDatabaseNotInitializedException extends RuntimeException {
	
	public MongoDatabaseNotInitializedException() {
	}
	
	public MongoDatabaseNotInitializedException(String message) {
		super(message);
	}
	
	public MongoDatabaseNotInitializedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MongoDatabaseNotInitializedException(Throwable cause) {
		super(cause);
	}
	
	public MongoDatabaseNotInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
