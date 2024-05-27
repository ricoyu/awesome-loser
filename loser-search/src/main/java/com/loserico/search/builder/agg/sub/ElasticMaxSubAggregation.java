package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * max子聚合
 * <p>
 * Copyright: (C), 2021-08-23 15:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticMaxSubAggregation extends SubAggregation {

	private SubAggregation parentAggregation;

	/**
	 * 聚合名字
	 */
	protected String name;

	/**
	 * 要对哪个字段聚合
	 */
	protected String field;

	public ElasticMaxSubAggregation(String name, String field) {
		this.name = name;
		this.field = field;
	}

	public ElasticMaxSubAggregation(SubAggregation parentAggregation, String name, String field) {
		this.parentAggregation = parentAggregation;
		this.name = name;
		this.field = field;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.max(name).field(field);
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
