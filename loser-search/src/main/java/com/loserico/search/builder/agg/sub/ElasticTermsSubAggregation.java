package com.loserico.search.builder.agg.sub;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

/**
 * Terms子聚合
 * <p>
 * Copyright: (C), 2021-08-23 15:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticTermsSubAggregation extends SubAggregation {
	
	private SubAggregation parentAggregation;
	
	/**
	 * 聚合名字
	 */
	protected String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	protected String field;
	
	private Integer size;
	
	public ElasticTermsSubAggregation(String name, String field) {
		this.name = name;
		this.field = field;
	}
	
	public ElasticTermsSubAggregation(SubAggregation parentAggregation, String name, String field) {
		this.parentAggregation = parentAggregation;
		this.name = name;
		this.field = field;
	}
	
	public ElasticTermsSubAggregation size(int size) {
		this.size = size;
		return this;
	}
	
	@Override
	public AggregationBuilder build() {
		TermsAggregationBuilder builder = AggregationBuilders.terms(name).field(field);
		if (this.size != null) {
			builder.size(this.size);
		}
		return builder;
	}
	
	@Override
	public SubAggregation and() {
		return parentAggregation;
	}
}
