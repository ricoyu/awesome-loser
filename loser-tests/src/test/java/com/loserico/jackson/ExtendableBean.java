package com.loserico.jackson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class ExtendableBean {
	public String name;
	private Map<String, String> properties = new HashMap<>();

	@JsonAnyGetter
	public Map<String, String> getProperties() {
		return properties;
	}

	public void add(String key, String value) {
		properties.put(key, value);
	}
}