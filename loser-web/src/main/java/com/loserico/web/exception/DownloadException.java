package com.loserico.web.exception;

/**
 * <p>
 * Copyright: (C), 2021-08-27 10:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DownloadException extends RuntimeException {
	
	public DownloadException() {
	}
	
	public DownloadException(String message) {
		super(message);
	}
	
	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DownloadException(Throwable cause) {
		super(cause);
	}
	
	public DownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
