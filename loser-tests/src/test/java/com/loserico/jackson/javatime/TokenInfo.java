package com.loserico.jackson.javatime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenInfo {

	private boolean active;
	
	@JsonProperty("client_id")
	private String clientId;
	
	private String[] scope;
	
	@JsonProperty("user_name")
	private String username;
	
	private String[] aud;
	
	private LocalDateTime exp;
	
	private String[] authorities;
 	
}