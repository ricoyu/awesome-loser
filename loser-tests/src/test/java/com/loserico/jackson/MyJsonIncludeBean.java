package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MyJsonIncludeBean {
	public int id;
	public String name;

	public MyJsonIncludeBean(int id, String name) {
		this.id = id;
		this.name = name;
	}
}