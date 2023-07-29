package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * Stats子聚合 
 * <p>
 * Copyright: Copyright (c) 2023-07-29 17:44
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticStatsSubAggregation extends SubAggregation {
	
	private SubAggregation parentAggregation;
	
	/**
	 * 聚合名字
	 */
	protected String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	protected String field;
	
	public ElasticStatsSubAggregation(String name, String field) {
		this.name = name;
		this.field = field;
	}
	
	public ElasticStatsSubAggregation(SubAggregation parentAggregation, String name, String field) {
		this.parentAggregation = parentAggregation;
		this.name = name;
		this.field = field;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.stats(name).field(field);
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
