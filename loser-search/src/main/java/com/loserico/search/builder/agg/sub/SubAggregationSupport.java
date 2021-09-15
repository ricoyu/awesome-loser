package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.BaseAggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-08-26 10:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SubAggregationSupport {
	
	/**
	 * 为aggregationBuilder添加subAggregations, SubAggregation可以是AggregationBuilder 或者 PipelineAggregationBuilder
	 * 
	 * @param aggregationBuilder
	 * @param subAggregations
	 */
	public static void addSubAggregations(AggregationBuilder aggregationBuilder, List<SubAggregation> subAggregations) {
		for (SubAggregation subAggregation : subAggregations) {
			BaseAggregationBuilder builder = subAggregation.build();
			if (builder instanceof AggregationBuilder) {
				aggregationBuilder.subAggregation((AggregationBuilder) builder);
			} else if (builder instanceof PipelineAggregationBuilder) {
				aggregationBuilder.subAggregation((PipelineAggregationBuilder) builder);
			}
		}
	}
}
