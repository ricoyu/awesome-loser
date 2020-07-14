package com.loserico.sharding.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 所有异常的父类
 * <p>
 * Copyright: (C), 2020/2/14 18:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoutingException extends RuntimeException {
	
	private Integer errorCode;
	
	private String errorMsg;
	
	public RoutingException(String message) {
		super(message);
	}
	
	public RoutingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RoutingException(Throwable cause) {
		super(cause);
	}
}
