package com.loserico.search.builder.agg.sub;

/**
 * <p>
 * Copyright: (C), 2021-08-23 16:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface AvgSubAggregation {
	
	/**
	 * 添加一个嵌套的AVG子聚合
	 * @param name
	 * @param field
	 * @return
	 */
	public ElasticSubAggregation avgSubAggregation(String name, String field);
}
