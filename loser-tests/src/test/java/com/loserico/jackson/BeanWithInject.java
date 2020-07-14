package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JacksonInject;

public class BeanWithInject {
	@JacksonInject
	public int id;
	public String name;
}