package com.loserico.search.builder;

import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.ContextType;
import com.loserico.search.enums.FieldType;
import com.loserico.search.support.FieldContext;
import com.loserico.search.support.FieldDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FieldDefBuilder {
	
	private AbstractMappingBuilder elasticMappingBuilder;
	
	private String fieldName;
	
	private FieldType fieldType;
	
	/**
	 * 如果这个字段是keyword类型, 并且需要对这个字段做聚合, 那么可以打开eagerGlobalOrdinals以提高性能
	 * Global ordinals are a data structure that is used to optimize the performance of aggregations.
	 * They are calculated lazily and stored in the JVM heap as part of the field data cache.
	 * For fields that are heavily used for bucketing aggregations, you can tell Elasticsearch to
	 * construct and cache the global ordinals before requests are received.
	 * <p>
	 * This should be done carefully because it will increase heap usage and can make refreshes take longer.
	 */
	private boolean eagerGlobalOrdinals = false;
	
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
	 * 字段是否要存储, 默认字段是不单独存储的, 字段值都是从_source字段中抽取的
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
	private AbstractMappingBuilder elasticMappingBuilder1;
	
	public FieldDefBuilder(String fieldName, FieldType fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	public FieldDefBuilder(AbstractMappingBuilder elasticMappingBuilder, String fieldName, FieldType fieldType) {
		this.elasticMappingBuilder = elasticMappingBuilder;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	public static FieldDefBuilder builder(String fieldName, FieldType fieldType) {
		return new FieldDefBuilder(fieldName, fieldType);
	}
	
	/**
	 * 如果这个字段是keyword类型, 并且需要对这个字段做聚合, 那么可以打开eagerGlobalOrdinals以提高性能
	 * Global ordinals are a data structure that is used to optimize the performance of aggregations.
	 * They are calculated lazily and stored in the JVM heap as part of the field data cache.
	 * For fields that are heavily used for bucketing aggregations, you can tell Elasticsearch to
	 * construct and cache the global ordinals before requests are received.
	 * <p>
	 * This should be done carefully because it will increase heap usage and can make refreshes take longer.
	 *
	 * @param eagerGlobalOrdinals
	 * @return
	 */
	public FieldDefBuilder eagerGlobalOrdinals(boolean eagerGlobalOrdinals) {
		this.eagerGlobalOrdinals = eagerGlobalOrdinals;
		return this;
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
	 * 如将enabled设置为false, 则无法进行搜索和聚合分析
	 *
	 * @param enabled
	 * @return
	 */
	public FieldDefBuilder enabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public FieldDefBuilder store(Boolean store) {
		this.store = store;
		return this;
	}
	
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
	 *
	 * @param nullValue
	 * @return FieldDefBuilder
	 */
	public FieldDefBuilder nullValue(Object nullValue) {
		this.nullValue = nullValue;
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
	 * @param fieldDefBuilder
	 * @return
	 */
	public FieldDefBuilder fields(FieldDefBuilder fieldDefBuilder) {
		this.fields.add(fieldDefBuilder.build());
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
	 *
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
	
	public AbstractMappingBuilder and() {
		FieldDef fieldDef = build();
		elasticMappingBuilder.field(fieldDef);
		return elasticMappingBuilder;
	}
	
	public FieldDefBuilder field(String fieldName, FieldType fieldType) {
		FieldDef fieldDef = build();
		elasticMappingBuilder.field(fieldDef);
		return this;
	}
	
	/**
	 * 发出请求, 这是一个终端方法
	 * @return boolean
	 */
	public boolean thenCreate() {
		FieldDef fieldDef = build();
		elasticMappingBuilder.field(fieldDef);
		
		if (elasticMappingBuilder instanceof ElasticIndexMappingBuilder) {
			return ((ElasticIndexMappingBuilder)elasticMappingBuilder).thenCreate();
		}
		
		if (elasticMappingBuilder instanceof ElasticPutMappingBuilder) {
			return ((ElasticPutMappingBuilder)elasticMappingBuilder).thenCreate();
		}
		
		if (elasticMappingBuilder instanceof ElasticIndexTemplateMappingBuilder) {
			return ((ElasticIndexTemplateMappingBuilder)elasticMappingBuilder).thenCreate();
		}
		
		throw new RuntimeException("Not recognized MappingBuild " + elasticMappingBuilder.getClass());
	}
	
	FieldDef build() {
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
		
		fieldDef.setAnalyzer(analyzer);
		fieldDef.setSearchAnalyzer(searchAnalyzer);
		fieldDef.setCopyTo(copyTo);
		fieldDef.setFields(fields);
		fieldDef.setFieldData(fieldData);
		fieldDef.setContexts(contexts);
		fieldDef.setEagerGlobalOrdinals(eagerGlobalOrdinals);
		fieldDef.setIndex(index);
		fieldDef.setEnabled(enabled);
		fieldDef.setStore(store);
		fieldDef.setNullValue(nullValue);
		
		return fieldDef;
	}
}
