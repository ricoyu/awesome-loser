package com.loserico.search.exception;

/**
 * 创建索引失败抛出该异常
 * <p>
 * Copyright: (C), 2020-12-03 20:40
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexCreateException extends RuntimeException {
	
	public IndexCreateException() {
		super();
	}
	
	public IndexCreateException(String message) {
		super(message);
	}
	
	public IndexCreateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IndexCreateException(Throwable cause) {
		super(cause);
	}
	
	protected IndexCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
