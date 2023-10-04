package com.loserico.common.lang.exception;

/**
 * <p>
 * Copyright: (C), 2023-10-02 20:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SqlParseException extends RuntimeException{
	
	public SqlParseException() {
	}
	
	public SqlParseException(String message) {
		super(message);
	}
	
	public SqlParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SqlParseException(Throwable cause) {
		super(cause);
	}

}
