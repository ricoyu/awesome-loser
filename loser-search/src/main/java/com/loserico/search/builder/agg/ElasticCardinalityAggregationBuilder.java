package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;

/**
 * 这个聚合是对字段去重后统计数量
 * <p>
 * Copyright: (C), 2021-06-18 9:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticCardinalityAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder {
	
	private ElasticCardinalityAggregationBuilder(String... indices) {
		this.indices = indices;
	}
	
	public static ElasticCardinalityAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticCardinalityAggregationBuilder(indices);
	}
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticCardinalityAggregationBuilder
	 */
	@Override
	public ElasticCardinalityAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	@Override
	public ElasticCardinalityAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.cardinality(name).field(field);
	}
	
	public Long get() {
		CardinalityAggregationBuilder arrregationBuilder = (CardinalityAggregationBuilder) build();
		
		SearchRequestBuilder builder = searchRequestBuilder();
		builder.addAggregation(arrregationBuilder).setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.cardinalityResult(aggregations);
	}
	
	
}
