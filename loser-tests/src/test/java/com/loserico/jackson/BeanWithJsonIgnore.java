package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BeanWithJsonIgnore {

	public int id;

	@JsonIgnore
	public String name;

	public BeanWithJsonIgnore(int id, String name) {
		this.id = id;
		this.name = name;
	}
}