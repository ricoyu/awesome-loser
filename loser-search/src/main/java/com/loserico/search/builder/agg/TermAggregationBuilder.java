package com.loserico.search.builder.agg;

/**
 * <p>
 * Copyright: (C), 2021-06-18 21:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface TermAggregationBuilder extends ElasticAggregationBuilder, Compositable{
	
	/**
	 * 配置聚合后返回的分桶的数量
	 * @param size
	 * @return TermAggregationBuilder
	 */
	public TermAggregationBuilder size(Integer size);
}
