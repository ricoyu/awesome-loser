package com.loserico.networking.enums;

/**
 * OAuth2 权限范围
 * <p>
 * Copyright: (C), 2020/5/5 16:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum Scope {
	
	READ("read"),
	WRITE("write");
	
	private Scope(String scope) {
		this.scope = scope;
	}
	
	private String scope;
	
	
	@Override
	public String toString() {
		return scope;
	}
}
