package com.loserico.search.enums;

/**
 * 定义文档字段类型 
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
	 * 精确匹配, 该类型字段不会被Analyze
	 */
	KEYWORD("keyword"), 
	
	DATE("date", "yyyy-MM-dd HH:mm:ss"), 
	
	LONG("long"), 
	
	INTEGER("integer"), 
	
	ARRAY("array"), 
	
	OBJECT("object"), 
	
	IP("ip"), 
	
	GEO_POINT("geo_point");
	
	/**
	 * 类型名称
	 */
	private String type;
	
	/**
	 * 该类型额外的一些属性, 对于date类型是起日期格式化串
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
