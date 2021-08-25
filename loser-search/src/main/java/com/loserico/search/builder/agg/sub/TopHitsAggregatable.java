package com.loserico.search.builder.agg.sub;

/**
 * 表示此聚合可以添加一个Top hits 子聚合
 * <p>
 * Copyright: (C), 2021-08-24 14:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface TopHitsAggregatable {
	
	/**
	 * 添加一个嵌套的Top Hits子聚合
	 * @param name
	 * @return
	 */
	public SubAggregation topHitsSubAggregation(String name);
}
