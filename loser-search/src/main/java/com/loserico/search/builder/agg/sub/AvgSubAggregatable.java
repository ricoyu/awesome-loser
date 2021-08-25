package com.loserico.search.builder.agg.sub;

/**
 * 表示实现这个接口的子聚合, 还可以添加AVG子聚合
 * <p>
 * Copyright: (C), 2021-08-23 16:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface AvgSubAggregatable {
	
	/**
	 * 添加一个嵌套的AVG子聚合
	 * @param name
	 * @param field
	 * @return
	 */
	public SubAggregation avgSubAggregation(String name, String field);
}
