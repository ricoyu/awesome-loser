package com.loserico.cloud.common;

import lombok.Data;

import java.util.Date;

@Data
public class TokenInfo {
	
	private boolean active;
	
	private String client_id;
	
	private String[] scope;
	
	private String username;
	
	private String[] aud;
	
	private Date exp;
	
	private String[] authorities;
	
}
