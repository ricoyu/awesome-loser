package com.loserico.security.vo;

import java.io.Serializable;

/**
 * 接收登录请求参数 
 * <p>
 * Copyright: Copyright (c) 2021-05-14 15:02
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LoginRequest implements Serializable {
	
	private static final long serialVersionUID = -6853466282355401731L;
	
	private String username;
	
	private String password;
	
	public LoginRequest() {
	}
	
	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
