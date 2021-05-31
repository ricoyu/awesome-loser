package com.loserico.search.annotation;

import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.FieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.loserico.search.enums.FieldType.TEXT;

/**
 * 标记这是一个Elasticsearch文档字段
 * <p>
 * Copyright: (C), 2021-05-06 11:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
	
	/**
	 * 字段名
	 */
	String value() default "";
	
	/**
	 * 字段类型, 比如 TEXT KEYWORD
	 */
	FieldType type() default TEXT;
	
	/**
	 * 如果这个字段是keyword类型, 并且需要对这个字段做聚合, 那么可以打开eagerGlobalOrdinals以提高性能<p>
	 * Global ordinals are a data structure that is used to optimize the performance of aggregations.
	 * They are calculated lazily and stored in the JVM heap as part of the field data cache.
	 * For fields that are heavily used for bucketing aggregations, you can tell Elasticsearch to
	 * construct and cache the global ordinals before requests are received.
	 * <p>
	 * This should be done carefully because it will increase heap usage and can make refreshes take longer.
	 */
	boolean eagerGlobalOrdinals() default false;
	
	/**
	 * 日期类型的话可以设置其日期格式
	 * yyyy-MM-dd HH:mm:ss
	 */
	String format() default "";
	
	/**
	 * index=false 表示不支持搜索, 只支持terms聚合
	 */
	boolean index() default true;
	
	/**
	 * 如将enabled设置为false, 则无法进行搜索和聚合分析
	 */	
	boolean enabled() default true;
	
	/**
	 * 字段是否要存储, 默认字段是不单独存储的, 字段值都是从_source字段中抽取的
	 */
	boolean store() default false;
	
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
	String nullValue() default "";
	
	/**
	 * 为该字段指定分词器
	 */
	Analyzer analyzer() default Analyzer.STANDARD;
	
	/**
	 * 指定检索时使用的分词器, 不指定的话采用跟analyzer一样的分词器<p>
	 * Ik分词在建立的时候要注意: 
	 * <ul>
	 *     <li/>建索引采用ik_max_word
	 *     <li/>检索采用ik_smart
	 * </ul>
	 */
	Analyzer searchAnalyzer() default Analyzer.STANDARD;
	
	/**
	 * 将字段的数值拷贝到目标字段, 实现类似 _all 的作用, 目标字段不出现在 _source 中<p>
	 * 即可以用copyTo字段来实现搜索, 但是返回的source里面是没有copyTo指定的这个字段的
	 */
	String copyTo() default "";

	
}
