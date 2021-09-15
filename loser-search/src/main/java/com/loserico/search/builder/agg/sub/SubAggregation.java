package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.BaseAggregationBuilder;

import java.util.ArrayList;
import java.util.List;

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
public abstract class SubAggregation {
	
	/**
	 * 这里表示aggregationBuilder的子聚合
	 */
	protected final List<SubAggregation> subAggregations = new ArrayList<>();
	
	public SubAggregation() {
		
	}
	
	/**
	 * 真正开始构建Elasticsearch的AggregationBuilder, 子聚合关系都建立起来了
	 * @return AggregationBuilder
	 */
	public abstract BaseAggregationBuilder build();
	
	/**
	 * 在子聚合中嵌套子聚合时, API调用可以通过and()返回上一次的子聚合, <br/>
	 * 然后上一级的子聚合可以继续添加子聚合
	 * @return SubAggregation
	 */
	public abstract SubAggregation and();
}
