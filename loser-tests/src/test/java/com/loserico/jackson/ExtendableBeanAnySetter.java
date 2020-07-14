package com.loserico.jackson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ExtendableBeanAnySetter {
	public String name;
	private Map<String, String> properties = new HashMap<>();

	@JsonAnySetter
	public void add(String key, String value) {
		getProperties().put(key, value);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}