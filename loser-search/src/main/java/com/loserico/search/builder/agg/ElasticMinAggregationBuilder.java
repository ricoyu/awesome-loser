package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;

/**
 * <p>
 * Copyright: (C), 2021-05-10 16:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticMinAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder {
	
	private ElasticMinAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticMinAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticMinAggregationBuilder(indices);
	}
	
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticMinAggregationBuilder
	 */
	@Override
	public ElasticMinAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.min(name).field(field);
	}
	
	@Override
	public ElasticMinAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	public Double get() {
		MinAggregationBuilder arrregationBuilder = (MinAggregationBuilder) build();
		
		SearchRequestBuilder builder = searchRequestBuilder();
		builder.addAggregation(arrregationBuilder).setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.minResult(aggregations);
	}
}
