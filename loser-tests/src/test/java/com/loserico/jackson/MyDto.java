package com.loserico.jackson;

public class MyDto {
	private String stringValue;
	private int intValue;
	private boolean booleanValue;
	
	public MyDto() {
		super();
	}

	public MyDto(String stringValue, int intValue, boolean booleanValue) {
		super();
		this.stringValue = stringValue;
		this.intValue = intValue;
		this.booleanValue = booleanValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
}
