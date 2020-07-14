package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "name", "id" })
public class MyBeanIgnoreAllAnnotation {
	public int id;
	public String name;

	public MyBeanIgnoreAllAnnotation(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public MyBeanIgnoreAllAnnotation() {
	}
}