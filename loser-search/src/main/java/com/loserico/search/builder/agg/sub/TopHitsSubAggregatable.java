package com.loserico.search.builder.agg.sub;

/**
 * 表示实现这个接口的子聚合, 还可以添加一个Top hits 子聚合
 * <p>
 * Copyright: (C), 2021-08-24 14:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface TopHitsSubAggregatable {
	
	/**
	 * 添加一个嵌套的Top Hits子聚合
	 * @param name
	 * @return
	 */
	public SubAggregation topHitsSubAggregation(String name);
}
