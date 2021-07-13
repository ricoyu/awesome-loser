package com.loserico.search.builder.agg;

import com.loserico.search.builder.agg.sub.SubHistogramAgg;

/**
 * <p>
 * Copyright: (C), 2021-07-12 18:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface SubAggable {
	
	/**
	 * 添加 Histogram 子聚合
	 * @param name
	 * @param field
	 * @return SubHistogramAgg
	 */
	public SubHistogramAgg subHistogram(String name, String field);
}
