package com.loserico.search.support;

import com.loserico.search.builder.FieldDefBuilder;
import com.loserico.search.enums.Analyzer;
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
	 * index=false 表示不支持搜索, 支持terms聚合
	 */
	private Boolean index;
	
	/**
	 * 如将enabled设置为false, 则无法进行搜索和聚合分析
	 */
	private Boolean enabled;
	
	/**
	 * 默认设置下字段可以被索引和搜索, 但是字段值没有被单独存储
	 * By default, field values are indexed to make them searchable, but they are not stored.
	 * This means that the field can be queried, but the original field value cannot be retrieved.
	 * <p>
	 * 但是因为字段默认就是_source的一部分, 而_source默认是存储的, 所以这样也OK
	 * Usually this doesn’t matter. The field value is already part of the _source field, which is stored by default. <p>
	 * <p>
	 * 就是说如果文档中某个字段内容很大, 比如content存了一整篇文章, 但是我们查询的时候只是想要查一下作者的信息, 那么就可以把author字段设置成store=true<p>
	 * 这样我们就能单独获取author字段, 而不是从很大的_source中抽取author字段<p>
	 * In certain situations it can make sense to store a field.
	 * For instance, if you have a document with a title, a date, and a very large content field,
	 * you may want to retrieve just the title and the date without having to extract those fields from a large _source field
	 */
	private Boolean store;
	
	/**
	 * 在数据建模时, 为字段设置null_value, 可以避免空值引起的聚合不准
	 * <pre>
	 * {@code
	 *   "mappings": {
	 *     "properties": {
	 *       "rating": {
	 *         "type": "float", 
	 *         "null_value": 0.0
	 *       }
	 *     }
	 *   }
	 * }    
	 * </pre>
	 * 如此, 当rating是null时, 实际插入ES的值将是0.0
	 */
	private Object nullValue;
	
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
	 * 如果这个字段是keyword类型, 并且需要对这个字段做聚合, 那么可以打开eagerGlobalOrdinals以提高性能
	 * 打开后一旦有文档写入, 这个字段都会被预加载以提高聚合性能
	 * Global ordinals are a data structure that is used to optimize the performance of aggregations.
	 * They are calculated lazily and stored in the JVM heap as part of the field data cache.
	 * For fields that are heavily used for bucketing aggregations, you can tell Elasticsearch to
	 * construct and cache the global ordinals before requests are received.
	 * <p>
	 * This should be done carefully because it will increase heap usage and can make refreshes take longer.
	 */
	private boolean eagerGlobalOrdinals = false;
	
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
	
	/**
	 * TODO
	 * 声明Parent Child关系
	 * key是parent名称, value是child名称
	 */
	private Map<String, String> relations = new HashMap<>();
	
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
		
		if (fieldType == FieldType.KEYWORD && eagerGlobalOrdinals) {
			defMap.put("eager_global_ordinals", true);
		}
		
		if (index != null) {
			defMap.put("index", index);
		}
		
		if (enabled != null) {
			defMap.put("enabled", enabled);
		}
		
		if (store != null) {
			defMap.put("store", store);
		}
		
		if (nullValue != null) {
			defMap.put("null_value", nullValue);
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
