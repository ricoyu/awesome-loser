package com.loserico.security.exception;

/**
 * 根据String token解析Jwt失败时抛出该异常, 该异常属于一种认证异常
 * <p>
 * Copyright: (C), 2020/5/23 16:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JwtTokenParseException extends RuntimeException {
	
	public JwtTokenParseException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public JwtTokenParseException(String msg) {
		super(msg);
	}
	
	public JwtTokenParseException(Throwable e) {
		super("", e);
	}
}
