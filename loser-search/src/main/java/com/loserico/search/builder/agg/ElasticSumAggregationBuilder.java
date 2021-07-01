package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2021-06-21 20:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticSumAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder {
	
	private ElasticSumAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticSumAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticSumAggregationBuilder(indices);
	}
	
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticSumAggregationBuilder
	 */
	@Override
	public ElasticSumAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.sum(name).field(field);
	}
	
	@Override
	public ElasticSumAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	public Double get() {
		SumAggregationBuilder arrregationBuilder = (SumAggregationBuilder)build();
		
		SearchRequestBuilder builder = searchRequestBuilder();
		builder.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.sumResult(aggregations);
	}
}
