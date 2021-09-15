package com.loserico.search.builder.agg.sub;

/**
 * 可以添加 Bucket Sort Aggregation
 * <p>
 * Copyright: (C), 2021-08-26 10:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface BucketSortPipelineSubAggregatable {
	
	public SubAggregation bucketSortPipelineSubAggregation(String name);
}
