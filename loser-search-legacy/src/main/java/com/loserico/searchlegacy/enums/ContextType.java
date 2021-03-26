package com.loserico.searchlegacy.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContextType {
	
	/**
	 * 任意的字符串
	 */
	CATEGORY("category"),
	
	/**
	 * 地理位置信息
	 */
	GEO("geo");
	
	@JsonValue
	private String type;
	
	private ContextType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
