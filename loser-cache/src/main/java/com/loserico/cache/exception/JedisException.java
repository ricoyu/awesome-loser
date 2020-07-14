package com.loserico.cache.exception;

/**
 * Jedis 抛出未知异常后由该异常包裹后重新抛出
 * <p>
 * Copyright: (C), 2020/4/13 15:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisException extends RuntimeException {
	
	public JedisException() {
		super();
	}
	
	public JedisException(String message) {
		super(message);
	}
	
	public JedisException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JedisException(Throwable cause) {
		super(cause);
	}
	
	protected JedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
