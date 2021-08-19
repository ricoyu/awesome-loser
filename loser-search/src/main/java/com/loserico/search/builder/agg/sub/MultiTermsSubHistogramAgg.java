package com.loserico.search.builder.agg.sub;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.builder.agg.ElasticMultiTermsAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.List;
import java.util.Map;

/**
 * 为Term聚合创建子Histogram聚合时返回这个对象
 * <p>
 * Copyright: (C), 2021-07-13 14:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MultiTermsSubHistogramAgg extends SubHistogramAgg {
	
	private ElasticMultiTermsAggregationBuilder aggregationBuilder;
	
	public MultiTermsSubHistogramAgg(ElasticMultiTermsAggregationBuilder aggregationBuilder, String name, String field) {
		this.aggregationBuilder = aggregationBuilder;
		this.name = name;
		this.field = field;
	}
	
	@Override
	public MultiTermsSubHistogramAgg interval(double interval) {
		super.interval(interval);
		return this;
	}
	
	@Override
	public MultiTermsSubHistogramAgg minDocCount(Integer minDocCount) {
		super.minDocCount(minDocCount);
		return this;
	}
	
	@Override
	public MultiTermsSubHistogramAgg extendedBounds(Long minBound, Long maxBound) {
		super.extendedBounds(minBound, maxBound);
		return this;
	}
	
	@Override
	public ElasticMultiTermsAggregationBuilder and() {
		AggregationBuilder subHistogramBuilder = build();
		ReflectionUtils.invokeMethod(aggregationBuilder, "subAggregation", subHistogramBuilder);
		return aggregationBuilder;
	}
	
	/**
	 * 代理主聚合的GET操作
	 * @param <T>
	 * @return Map<String, T>
	 */
	public <T> List<Map<String, T>> thenGet() {
		return and().get();
	}
}
