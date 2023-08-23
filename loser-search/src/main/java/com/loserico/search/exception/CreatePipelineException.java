package com.loserico.search.exception;

/**
 * <p>
 * Copyright: (C), 2021-04-29 17:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CreatePipelineException extends RuntimeException {
	
	public CreatePipelineException() {
	}
	
	public CreatePipelineException(String message) {
		super(message);
	}
	
	public CreatePipelineException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CreatePipelineException(Throwable cause) {
		super(cause);
	}
	
	public CreatePipelineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
