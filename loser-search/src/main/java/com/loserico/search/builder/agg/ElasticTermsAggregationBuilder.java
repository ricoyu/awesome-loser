package com.loserico.search.builder.agg;

import com.loserico.search.ElasticUtils;
import com.loserico.search.vo.AggResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.util.ArrayList;
import java.util.List;

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
public class ElasticTermsAggregationBuilder extends AbstractAggregationBuilder {
	
	/**
	 * 这个是限制返回桶的数量, 如果总共有10个桶, 但是size设为5, 那么聚合结果中只会返回前5个桶
	 */
	private Integer size;
	
	private String[] indices;
	
	/**
	 * 聚合名字
	 */
	private String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	private String field;
	
	private ElasticTermsAggregationBuilder(String[] indices) {
		this.indices = indices;
	}
	
	public static ElasticTermsAggregationBuilder instance(String... indices) {
		if (indices == null || indices.length == 0) {
			throw new IllegalArgumentException("indices cannot be null!");
		}
		return new ElasticTermsAggregationBuilder(indices);
	}
	
	/**
	 * 给聚合起个名字, 后续获取聚合数据时需要
	 *
	 * @param name
	 * @return ElasticTermsAggregationBuilder
	 */
	public ElasticTermsAggregationBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 要对哪个字段聚合, 不能对TEXT类型字段做聚合, 不过可以用field.keyword代替
	 *
	 * @param field
	 * @return ElasticTermsAggregationBuilder
	 */
	public ElasticTermsAggregationBuilder field(String field) {
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
	
	public List<AggResult> get() {
		TermsAggregationBuilder arrregationBuilder = AggregationBuilders.terms(name).field(field);
		if (size != null) {
			arrregationBuilder.size(size);
		}
		
		SearchRequestBuilder builder = ElasticUtils.client.prepareSearch(indices)
				.addAggregation(arrregationBuilder)
				.setSize(0);
		logDsl(builder);
		
		SearchResponse searchResponse = builder.get();
		Aggregations aggregations = searchResponse.getAggregations();
		
		List<AggResult> aggResults = new ArrayList<>();
		for (Aggregation aggregation : aggregations) {
			List<Bucket> buckets = ((StringTerms) aggregation).getBuckets();
			for (Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				log.info("Bucket: {}, Doc Count: {}", key, docCount);
				aggResults.add(new AggResult(key, docCount));
			}
		}
		
		return aggResults;
	}
}
