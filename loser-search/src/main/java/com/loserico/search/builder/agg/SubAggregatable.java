package com.loserico.search.builder.agg;

import com.loserico.search.builder.agg.sub.SubAggregation;

/**
 * 表示具有添加子聚合的能力
 * <p>
 * Copyright: (C), 2021-07-12 18:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface SubAggregatable {
	
	/**
	 * 添加 子聚合
	 * @param subAggregation
	 * @return
	 */
	public ElasticAggregationBuilder subAggregation(SubAggregation subAggregation);
	
}
