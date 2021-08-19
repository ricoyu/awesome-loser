package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;

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
public class ElasticAvgAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder {
	
	private ElasticAvgAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticAvgAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticAvgAggregationBuilder(indices);
	}
	
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticAvgAggregationBuilder
	 */
	@Override
	public ElasticAvgAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	@Override
	public ElasticAvgAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	/**
	 * 聚合返回的结果中是否要包含总命中数 
	 * @param fetchTotalHits
	 * @return ElasticAvgAggregationBuilder
	 */
	public ElasticAvgAggregationBuilder fetchTotalHits(boolean fetchTotalHits) {
		this.fetchTotalHits = fetchTotalHits;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		return AggregationBuilders.avg(name).field(field);
	}
	
	public Double get() {
		AvgAggregationBuilder arrregationBuilder = (AvgAggregationBuilder)build();
		
		SearchRequestBuilder builder = searchRequestBuilder();
		builder.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.avgResult(aggregations);
	}
}
