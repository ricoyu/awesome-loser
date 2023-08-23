package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import com.loserico.search.support.StatsAggResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.StatsAggregationBuilder;

/**
 * stats聚合
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
public class ElasticStatsAggregationBuilder extends AbstractAggregationBuilder implements ElasticAggregationBuilder {
	
	private ElasticStatsAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticStatsAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticStatsAggregationBuilder(indices);
	}
	
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticMinAggregationBuilder
	 */
	@Override
	public ElasticStatsAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	/**
	 * 聚合返回的结果中是否要包含总命中数 
	 * @param fetchTotalHits
	 * @return ElasticMaxAggregationBuilder
	 */
	public ElasticStatsAggregationBuilder fetchTotalHits(boolean fetchTotalHits) {
		this.fetchTotalHits = fetchTotalHits;
		return this;
	}
	
	@Override
	public ElasticStatsAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	@Override
	public org.elasticsearch.search.aggregations.AggregationBuilder build() {
		return AggregationBuilders.stats(name).field(field);
	}
	
	public StatsAggResult get() {
		StatsAggregationBuilder arrregationBuilder = (StatsAggregationBuilder)build();
		
		SearchRequestBuilder builder = searchRequestBuilder();
		builder.addAggregation(arrregationBuilder).setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		addTotalHitsToThreadLocal(searchResponse);
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.statsResult(aggregations);
	}
}
