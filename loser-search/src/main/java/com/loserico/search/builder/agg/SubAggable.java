package com.loserico.search.builder.agg;

import com.loserico.search.builder.agg.sub.SubAvgAgg;
import com.loserico.search.builder.agg.sub.SubDateHistogramAgg;
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
	public default SubHistogramAgg subHistogram(String name, String field) {
		throw new UnsupportedOperationException("这个功能还没有实现呢老铁~");
	};
	
	/**
	 * 添加 DateHistogram 子聚合
	 * @param name
	 * @param field
	 * @return SubDateHistogramAgg
	 */
	public default SubDateHistogramAgg subDateHistogram(String name, String field) {
		throw new UnsupportedOperationException("这个功能还没有实现呢老铁~");
	}
	
	/**
	 * 添加AVG子聚合
	 * @param name
	 * @param field
	 * @return SubAvgAgg
	 */
	public default SubAvgAgg subAvg(String name, String field) {
		throw new UnsupportedOperationException("这个功能还没有实现呢老铁~");
	}
}
