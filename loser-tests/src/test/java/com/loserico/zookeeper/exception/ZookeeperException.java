package com.loserico.zookeeper.exception;

/**
 * 操作zookeeper时抛出的通用异常
 * <p>
 * Copyright: Copyright (c) 2019-04-08 15:12
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class ZookeeperException extends RuntimeException {

	private static final long serialVersionUID = 1681762559216558634L;

	public ZookeeperException() {
		super();
	}

	public ZookeeperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ZookeeperException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZookeeperException(String message) {
		super(message);
	}

	public ZookeeperException(Throwable cause) {
		super(cause);
	}

	
}
