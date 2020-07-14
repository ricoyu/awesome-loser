package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MyBeanJsonSetter {
	public int id;
	private String myName;

	@JsonSetter("name")
	public void setTheName(String s) {
		this.myName = s;
	}

	public String getTheName() {
		return this.myName;
	}
}