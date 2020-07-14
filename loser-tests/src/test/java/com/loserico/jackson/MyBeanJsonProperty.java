package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyBeanJsonProperty {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getTheName() {
		return name;
	}

	public void setTheName(String name) {
		this.name = name;
	}

	public MyBeanJsonProperty(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public MyBeanJsonProperty() {
	}

}