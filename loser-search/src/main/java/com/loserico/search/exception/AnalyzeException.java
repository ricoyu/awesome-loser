package com.loserico.search.exception;

/**
 * 分析失败时抛出该异常
 * <p>
 * Copyright: (C), 2021-02-12 15:42
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AnalyzeException extends RuntimeException {
	
	public AnalyzeException() {
	}
	
	public AnalyzeException(String message) {
		super(message);
	}
	
	public AnalyzeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AnalyzeException(Throwable cause) {
		super(cause);
	}
	
	public AnalyzeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
