package com.loserico.search.builder.agg;

/**
 * 表示支持添加子聚合
 * <p>
 * Copyright: (C), 2021-07-12 18:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface SubAggregateable {
	
	/**
	 * 添加一个子聚合<br/>
	 * SubAggregation对象可以通过SubAggregation#instance(AggregationBuilder)来构造
	 * @param subAggregation
	 * @return ElasticAggregationBuilder
	 */
	public default ElasticAggregationBuilder subAggregation(SubAggregation subAggregation) {
		throw new UnsupportedOperationException("这个功能还在等待你来实现呢老铁~");
	}
	
}
