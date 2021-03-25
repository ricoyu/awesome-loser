package com.loserico.networking.enums;

/**
 * OAuth2授权类型
 * <p>
 * Copyright: (C), 2020/5/5 16:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum GrantType {
	
	/**
	 * 密码模式
	 */
	PASSWORD("password"),
	
	/**
	 * 授权码模式
	 */
	AUTHORIZATION_CODE("authorization_code"),
	
	REFRESH_TOKEN("refresh_token");
	
	private GrantType(String grantType) {
		this.grantType = grantType;
	}
	
	private String grantType;
	
	
	@Override
	public String toString() {
		return grantType;
	}
}
