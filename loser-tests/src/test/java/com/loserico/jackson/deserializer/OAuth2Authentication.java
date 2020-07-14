package com.loserico.jackson.deserializer;

/**
 * <p>
 * Copyright: (C), 2020/4/29 18:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OAuth2Authentication {
	
	private String name;
	
	private Integer age;
	
	public OAuth2Authentication(String name, Integer age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
}
