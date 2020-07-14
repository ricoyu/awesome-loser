package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;

public class MyBean {
	public int id;
	private String name;

	/*
	 * In the following example – we specify the method getTheName() as the getter
	 * method of name property of MyBean entity:
	 */
	@JsonGetter("name")
	public String getTheName() {
		return name;
	}

	public MyBean(int id, String name) {
		this.id = id;
		this.name = name;
	}

}