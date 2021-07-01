package com.loserico.search.builder.agg;

import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * <p>
 * Copyright: (C), 2021-06-18 21:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface ElasticAggregationBuilder {
	
	/**
	 * 要对哪个字段聚合, 不能对TEXT类型字段做聚合, 不过可以用field.keyword代替
	 *
	 * @param name  给聚合起个名字
	 * @param field 对哪个字段做聚合
	 * @return ElasticAvgAggregationBuilder
	 */
	public ElasticAggregationBuilder of(String name, String field);
	
	public AggregationBuilder build();
	
}
