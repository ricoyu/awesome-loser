package com.loserico.search.builder.agg;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.support.AggResultSupport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.Map;

/**
 * Terms 聚合
 * <p>
 * Copyright: (C), 2021-05-10 11:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticTermsAggregationBuilder extends AbstractAggregationBuilder implements TermAggregationBuilder, Compositable {
	
	/**
	 * 这个是限制返回桶的数量, 如果总共有10个桶, 但是size设为5, 那么聚合结果中只会返回前5个桶
	 */
	private Integer size;
	
	/**
	 * 帮助解决Terms不准的问题<br/>
	 * Terms 聚合分析不准的原因, 数据分散在多个不同的分片上, Coordinating Node 无法获取数据全貌<br/>
	 * 解决方案 1: 当数据量不大时, 设置Primary Shard为1; 实现准确性<br/>
	 * 方案 2: 在分布式数据上, 设置shard_size参数, 提高精确度<br/>
	 * 原理: 每次从Shard上额外多获取数据, 提升准确率
	 */
	private Integer shardSize;
	
	private ElasticTermsAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticTermsAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticTermsAggregationBuilder(indices);
	}
	
	@Override
	public ElasticTermsAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		super.setQuery(queryBuilder);
		return this;
	}
	
	@Override
	public ElasticTermsAggregationBuilder of(String name, String field) {
		this.name = name;
		this.field = field;
		return this;
	}
	
	/**
	 * 这个是限制返回桶的数量, 如果总共有10个桶, 但是size设为5, 那么聚合结果中只会返回前5个桶
	 *
	 * @param size
	 * @return ElasticTermsAggregationBuilder
	 */
	public ElasticTermsAggregationBuilder size(Integer size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 帮助解决Terms不准的问题<br/>
	 * Terms 聚合分析不准的原因, 数据分散在多个不同的分片上, Coordinating Node 无法获取数据全貌<br/>
	 * 解决方案 1: 当数据量不大时, 设置Primary Shard为1; 实现准确性<br/>
	 * 方案 2: 在分布式数据上, 设置shard_size参数, 提高精确度<br/>
	 * 原理: 每次从Shard上额外多获取数据, 提升准确率
	 *
	 * @param shardSize
	 * @return ElasticTermsAggregationBuilder
	 */
	public ElasticTermsAggregationBuilder shardSize(Integer shardSize) {
		this.shardSize = shardSize;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(name).field(field);
		if (size != null) {
			aggregationBuilder.size(size);
		}
		if (shardSize != null) {
			aggregationBuilder.shardSize(shardSize);
		}
		return aggregationBuilder;
	}
	
	@Override
	public ElasticCompositeAggregationBuilder and() {
		compositeAggregationBuilder.add(build());
		return compositeAggregationBuilder;
	}
	
	public Map<String, Object> get() {
		TermsAggregationBuilder arrregationBuilder = (TermsAggregationBuilder) build();
		
		SearchRequestBuilder searchRequestBuilder = searchRequestBuilder();
		
		SearchRequestBuilder builder = searchRequestBuilder
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		SearchResponse searchResponse = builder.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		return AggResultSupport.termsResult(aggregations);
	}
}
