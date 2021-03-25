package com.loserico.networking.enums;

/**
 * HTTP请求方法
 * <p>
 * Copyright: Copyright (c) 2021-03-22 16:28
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum HttpMethod {
	
	GET,
	
	POST,
	
	PUT,
	
	DELETE,
	
	OPTIONS,
	
	TRACE,
	
	HEAD,
	
	PATCH;
	
	@Override
	public String toString() {
		return this.name();
	}
}
