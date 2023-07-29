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
public final class SubAggregations {
	
	public static ElsticHistogramSubAggregation histogram(String name, String field) {
		return new ElsticHistogramSubAggregation(name, field);
	}
	
	public static ElasticDateHistogramSubAggregation dateHistogram(String name, String field) {
		return new ElasticDateHistogramSubAggregation(name, field);
	}
	
	public static ElasticAvgSubAggregation avg(String name, String field) {
		return new ElasticAvgSubAggregation(name, field);
	}
	
	public static ElasticTermsSubAggregation terms(String name, String field) {
		return new ElasticTermsSubAggregation(name, field);
	}
	
	public static ElasticSumSubAggregation sum(String name, String field) {
		return new ElasticSumSubAggregation(name, field);
	}
	
	/**
	 * 添加Top Hits子聚合
	 * @param name
	 * @return ElasticTopHitsSubAggregation
	 */
	public static ElasticTopHitsSubAggregation topHits(String name) {
		return new ElasticTopHitsSubAggregation(name);
	}
	
	/**
	 * 添加Bucket Sort子聚合
	 * @param name
	 * @return ElasticBucketSortSubAggregation
	 */
	public static ElasticBucketSortSubAggregation bucketSort(String name) {
		return new ElasticBucketSortSubAggregation(name);
	}
}
