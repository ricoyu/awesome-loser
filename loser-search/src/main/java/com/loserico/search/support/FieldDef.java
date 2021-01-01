package com.loserico.search.support;

import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.FieldType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 封装字段定义 
 * <p>
 * Copyright: Copyright (c) 2020-12-28 11:03
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class FieldDef {
	
	/**
	 * 字段名
	 */
	private String fieldName;
	
	/**
	 * 字段类型
	 */
	private FieldType fieldType;
	
	/**
	 * 日期类型的话可以设置其日期格式
	 * yyyy-MM-dd HH:mm:ss
	 */
	private String format;
	
	/**
	 * 该字段是否可以被搜索
	 */
	private Boolean index;
	
	/**
	 * 为该字段指定分词器
	 */
	private Analyzer analyzer;
	
	public FieldDef(String fieldName, FieldType fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	public FieldDef(String fieldName, FieldType fieldType, Boolean index) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.index = index;
	}
	
	public static FieldDefBuilder builder(String fieldName, FieldType fieldType) {
		return new FieldDefBuilder(fieldName, fieldType);
	}
	
	public static final class FieldDefBuilder {
		
		private String fieldName;
		
		private FieldType fieldType;
		
		/**
		 * 日期类型的话可以设置其日期格式
		 * yyyy-MM-dd HH:mm:ss
		 */
		private String format;
		
		/**
		 * 该字段是否可以被搜索
		 */
		private Boolean index;
		
		/**
		 * 为该字段指定分词器
		 */
		private Analyzer analyzer;
		
		public FieldDefBuilder(String fieldName, FieldType fieldType) {
			this.fieldName = fieldName;
			this.fieldType = fieldType;
		}
		
		/**
		 * 日期类型的话可以设置其日期格式
		 * yyyy-MM-dd HH:mm:ss
		 */
		public FieldDefBuilder format(String format) {
			this.format = format;
			return this;
		}
		
		/**
		 * 该字段是否可以被搜索, 不设置默认为true
		 * @param index
		 * @return
		 */
		public FieldDefBuilder index(Boolean index) {
			this.index = index;
			return this;
		}
		
		/**
		 * 为该字段指定分词器
		 * @param analyzer
		 * @return
		 */
		public FieldDefBuilder analyzer(Analyzer analyzer) {
			this.analyzer = analyzer;
			return this;
		}
		
		public FieldDef build() {
			FieldDef fieldDef = new FieldDef(fieldName, fieldType);
			//默认用DATE类型的默认格式
			if (fieldType == FieldType.DATE) {
				fieldDef.setFormat(fieldType.getProperty().toString());
			}
			//如果显式设置了格式, 那么使用显式设置的格式
			if (format != null) {
				fieldDef.setFormat(format);
			}
			
			if (index != null) {
				fieldDef.setIndex(index);
			}
			
			fieldDef.analyzer = analyzer;
			
			return fieldDef;
		}
	}
	
	/**
	 * 输出字段定义的Map, 对应的JSON对象类似这样
	 * {
	 * "type": "date",
	 * "index": true,
	 * "format": "yyyy-MM-dd"
	 * }
	 *
	 * @return
	 */
	public Map<String, Object> toDefMap() {
		Map<String, Object> defMap = new HashMap<>();
		defMap.put("type", fieldType.toString());
		if (index != null) {
			defMap.put("index", index);
		}
		if (isNotBlank(format)) {
			defMap.put("format", format);
		}
		if (analyzer != null) {
			defMap.put("analyzer", analyzer);
		}
		
		return defMap;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FieldDef)) {
			return false;
		}
		
		FieldDef fieldDef = (FieldDef) obj;
		return this.fieldName.equals(fieldDef.getFieldName());
	}
}
