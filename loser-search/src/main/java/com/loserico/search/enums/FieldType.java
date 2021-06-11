package com.loserico.search.enums;

/**
 * 定义文档字段类型 
 * https://www.elastic.co/guide/en/elasticsearch/reference/7.x/mapping-types.html
 * 
 * <p>
 * Copyright: Copyright (c) 2020-12-26 10:00
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum FieldType {
	
	/**
	 * 支持全文搜索
	 */
	TEXT("text"),
	
	/**
	 * Used for auto-complete suggestions.
	 */
	COMPLETION("completion"),
	
	/**
	 * text-like type for as-you-type completion.
	 */
	SEARCH_AS_YOU_TYPE("search_as_you_type"),
	
	/**
	 * 精确匹配, 该类型字段不会被Analyze
	 */
	KEYWORD("keyword"), 
	
	DATE("date", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis||strict_date_optional_time"), 
	
	LONG("long"), 
	
	INTEGER("integer"),
	
	FLOAT("float"),
	
	DOUBLE("double"),
	
	BOOLEAN("boolean"),
	
	OBJECT("object"), 
	
	IP("ip"), 
	
	GEO_POINT("geo_point"),
	
	/**
	 * Binary value encoded as a Base64 string
	 * 二进制类型以base64方式存储
	 */
	BINARY("binary"),
	
	/**
	 * 嵌套对象
	 */
	NESTED("nested"),
	
	/**
	 * 文档的父子关系
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.x/parent-join.html
	 */
	JOIN("join");
	
	/**
	 * 类型名称
	 */
	private String type;
	
	/**
	 * 该类型额外的一些属性
	 */
	private Object property;
	
	private FieldType(String type) {
		this.type = type;
	}
	
	private FieldType(String type, Object property) {
		this.type = type;
		this.property = property;
	}
	
	public Object getProperty() {
		return property;
	}
	
	
	@Override
	public String toString() {
		return type;
	}
}
