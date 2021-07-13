package com.loserico.search.builder.agg;

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
	
	public default ElasticSubAggBuilder subAgg(String name) {
		return ElasticSubAggBuilder.instance(name);
	}
}
