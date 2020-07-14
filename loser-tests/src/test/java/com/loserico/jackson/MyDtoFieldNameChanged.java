package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyDtoFieldNameChanged {
	private String stringValue;

	@JsonProperty("strValue")
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}