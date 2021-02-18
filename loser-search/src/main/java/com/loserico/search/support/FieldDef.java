package com.loserico.search.support;

import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.ContextType;
import com.loserico.search.enums.FieldType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 封装字段定义
 * <p>
 * Copyright: Copyright (c) 2020-12-28 11:03
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
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
	 * 将字段的数值拷贝到目标字段, 实现类似 _all 的作用, 目标字段不出现在 _source 中
	 * 即可以用copyTo字段来实现搜索, 但是返回的source里面是没有copyTo指定的这个字段的
	 */
	private String copyTo;
	
	/**
	 * 为该字段指定分词器
	 */
	private Analyzer analyzer;
	
	/**
	 * 指定检索时使用的分词器, 不指定的话采用跟analyzer一样的分词器
	 * Ik分词在建立的时候要注意: 建索引采用ik_max_word 检索采用ik_smart
	 */
	private Analyzer searchAnalyzer;
	
	/**
	 * https://www.elastic.co/guide/en/elasticsearch/reference/7.10/text.html#fielddata-mapping-param
	 * 不建议开启, 会影响性能; 要对text类型做聚合, 排序可以使用多字段特性, 比如content.keyword
	 * For example, a full text field like New York would get analyzed as new and york. To aggregate on these tokens requires field data.
	 */
	private boolean fieldData = false;
	
	/**
	 * Elasticsearch多字段特性
	 */
	private List<FieldDef> fields = new ArrayList<>();
	
	/**
	 * 实现 Context Suggester 时需要加入上下文信息<br/>
	 * 可以定义两种类型的 Context
	 * <ul>
	 * <li/>Category 任意的字符串
	 * <li/>Geo      地理位置信息
	 * <ul/>
	 */
	private List<Map<String, String>> contexts;
	
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
		
		/**
		 * 指定检索时使用的分词器, 不指定的话采用跟analyzer一样的分词器
		 * Ik分词在建立的时候要注意: 建索引采用ik_max_word 检索采用ik_smart
		 */
		private Analyzer searchAnalyzer;
		
		/**
		 * 将字段的数值拷贝到目标字段, 实现类似 _all 的作用, 目标字段不出现在 _source 中
		 * 即可以用copyTo字段来实现搜索, 但是返回的source里面是没有copyTo指定的这个字段的
		 */
		private String copyTo;
		
		/**
		 * https://www.elastic.co/guide/en/elasticsearch/reference/7.10/text.html#fielddata-mapping-param
		 * 不建议开启, 会影响性能; 要对text类型做聚合, 排序可以使用多字段特性, 比如content.keyword
		 * For example, a full text field like New York would get analyzed as new and york. To aggregate on these tokens requires field data.
		 */
		private boolean fieldData = false;
		
		/**
		 * Elasticsearch多字段特性
		 */
		private List<FieldDef> fields = new ArrayList<>();
		
		/**
		 * 实现 Context Suggester 时需要加入上下文信息<br/>
		 * 可以定义两种类型的 Context
		 * <ul>
		 * <li/>Category 任意的字符串
		 * <li/>Geo      地理位置信息
		 * <ul/>
		 */
		private List<Map<String, String>> contexts;
		
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
		 *
		 * @param index
		 * @return
		 */
		public FieldDefBuilder index(Boolean index) {
			this.index = index;
			return this;
		}
		
		/**
		 * 为该字段指定分词器
		 *
		 * @param analyzer
		 * @return
		 */
		public FieldDefBuilder analyzer(Analyzer analyzer) {
			this.analyzer = analyzer;
			return this;
		}
		
		/**
		 * 指定检索时使用的分词器, 不指定的话采用跟analyzer一样的分词器
		 * Ik分词在建立的时候要注意: 建索引采用ik_max_word 检索采用ik_smart
		 *
		 * @param searchAnalyzer
		 * @return FieldDefBuilder
		 */
		public FieldDefBuilder searchAnalyzer(Analyzer searchAnalyzer) {
			this.searchAnalyzer = searchAnalyzer;
			return this;
		}
		
		/**
		 * 将字段的数值拷贝到目标字段, 实现类似 _all 的作用, 目标字段不出现在 _source 中
		 * 即可以用copyTo字段来实现搜索, 但是返回的source里面是没有copyTo指定的这个字段的
		 *
		 * @param destField
		 * @return FieldDefBuilder
		 */
		public FieldDefBuilder copyTo(String destField) {
			this.copyTo = destField;
			return this;
		}
		
		/**
		 * 为字段添加多字段特性
		 *
		 * @param fieldDef
		 * @return
		 */
		public FieldDefBuilder fields(FieldDef fieldDef) {
			this.fields.add(fieldDef);
			return this;
		}
		
		/**
		 * https://www.elastic.co/guide/en/elasticsearch/reference/7.10/text.html#fielddata-mapping-param
		 * 不建议开启, 会影响性能; 要对text类型做聚合, 排序可以使用多字段特性, 比如content.keyword
		 * For example, a full text field like New York would get analyzed as new and york. To aggregate on these tokens requires field data.
		 */
		public FieldDefBuilder fieldData(boolean fieldData) {
			this.fieldData = fieldData;
			return this;
		}
		
		/**
		 * 实现 Context Suggester时, 需要设置Mapping, 加入Context信息
		 * @param type
		 * @param name
		 * @return
		 */
		public FieldDefBuilder addContext(ContextType type, String name) {
			if (contexts == null) {
				contexts = new ArrayList<>();
			}
			
			contexts.add(new FieldContext(type, name).toMap());
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
			fieldDef.searchAnalyzer = searchAnalyzer;
			fieldDef.copyTo = copyTo;
			fieldDef.fields = fields;
			fieldDef.fieldData = fieldData;
			fieldDef.contexts = contexts;
			
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
		if (searchAnalyzer != null) {
			defMap.put("search_analyzer", searchAnalyzer);
		}
		if (copyTo != null) {
			defMap.put("copy_to", copyTo);
		}
		if (fieldData) {
			defMap.put("fieldData", fieldData);
		}
		if (fields != null && !fields.isEmpty()) {
			Map<String, Object> fieldsMap = new HashMap<>(fields.size());
			for (FieldDef fieldDef : fields) {
				fieldsMap.put(fieldDef.fieldName, fieldDef.toDefMap());
			}
			defMap.put("fields", fieldsMap);
		}
		if (contexts != null) {
			defMap.put("contexts", contexts);
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
