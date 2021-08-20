package com.loserico.search.builder.agg;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 表示这是一个子聚合
 * 子聚合还可以添加子聚合
 * <p>
 * Copyright: (C), 2021-08-20 15:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SubAggregation {
	
	/**
	 * 这个是Elasticsearch官方API的AggregationBuilder
	 */
	private AggregationBuilder aggregationBuilder;
	
	/**
	 * 这里表示aggregationBuilder的子聚合
	 */
	private final List<SubAggregation> subAggregations = new ArrayList<>();
	
	private SubAggregation() {
		
	}
	
	/**
	 * 提示: 通过AggregationBuilders这个工厂类来创建AggregationBuilder
	 *
	 * @param aggregationBuilder
	 * @return
	 */
	public static SubAggregation instance(AggregationBuilder aggregationBuilder) {
		Objects.requireNonNull(aggregationBuilder, "aggregationBuilder cannot be null!");
		SubAggregation subAggregation = new SubAggregation();
		subAggregation.aggregationBuilder = aggregationBuilder;
		return subAggregation;
	}
	
	/**
	 * 为这个子聚合再添加子聚合
	 *
	 * @param aggregationBuilder
	 * @return SubAggregation
	 */
	public SubAggregation subAggregation(AggregationBuilder aggregationBuilder) {
		Objects.requireNonNull(aggregationBuilder, "aggregationBuilder cannot be null!");
		SubAggregation subAggregation = new SubAggregation();
		subAggregation.aggregationBuilder = aggregationBuilder;
		this.subAggregations.add(subAggregation);
		return this;
	}
	
	/**
	 * 如果要嵌套多级子聚合的话, 用这个方法
	 *
	 * @param subAggregation
	 * @return
	 */
	public SubAggregation subAggregation(SubAggregation subAggregation) {
		Objects.requireNonNull(subAggregation, "subAggregation cannot be null!");
		this.subAggregations.add(subAggregation);
		return this;
	}
	
	/**
	 * 真正开始构建Elasticsearch的AggregationBuilder, 子聚合关系都建立起来了
	 * @return AggregationBuilder
	 */
	public AggregationBuilder build() {
		for (SubAggregation subAggregation : subAggregations) {
			aggregationBuilder.subAggregation(subAggregation.build());
		}
		
		return aggregationBuilder;
	}
}
