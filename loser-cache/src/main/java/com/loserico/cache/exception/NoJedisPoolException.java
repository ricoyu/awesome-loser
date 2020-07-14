package com.loserico.cache.exception;

/**
 * 没有建立任何Jedis Pool时, 会抛出该异常
 * <p>
 * Copyright: Copyright (c) 2019-06-03 10:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class NoJedisPoolException extends RuntimeException {

	private static final long serialVersionUID = -3791861066867554545L;

	public NoJedisPoolException() {
	}

	public NoJedisPoolException(String message) {
		super(message);
	}

	public NoJedisPoolException(Throwable cause) {
		super(cause);
	}

	public NoJedisPoolException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoJedisPoolException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
