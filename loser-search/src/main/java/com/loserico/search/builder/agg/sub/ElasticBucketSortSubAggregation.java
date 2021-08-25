package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * <p>
 * Copyright: (C), 2021-08-25 14:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticBucketSortSubAggregation extends SubAggregation {
	
	
	private SubAggregation parentAggregation;
	
	public ElasticBucketSortSubAggregation() {
	}
	
	@Override
	public AggregationBuilder build() {
		return null;
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
