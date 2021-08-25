package com.loserico.search.builder.agg.sub;

/**
 * <p>
 * Copyright: (C), 2021-08-23 14:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ElasticSubAggregations {
	
	public static ElsticHistogramSubAggregation histogram(String name, String field) {
		return new ElsticHistogramSubAggregation(name, field);
	}
	
	public static ElasticDateHistogramSubAggregation dateHistogram(String name, String field) {
		return new ElasticDateHistogramSubAggregation(name, field);
	}
	
	public static ElasticAvgSubAggregation avg(String name, String field) {
		return new ElasticAvgSubAggregation(name, field);
	}
	
	/**
	 * 添加Top Hits子聚合
	 * @param name
	 * @return ElasticTopHitsSubAggregation
	 */
	public static ElasticTopHitsSubAggregation topHits(String name) {
		return new ElasticTopHitsSubAggregation(name);
	}
}
