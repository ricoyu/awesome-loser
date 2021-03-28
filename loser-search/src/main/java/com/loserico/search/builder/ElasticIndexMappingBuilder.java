package com.loserico.search.builder;

import com.loserico.search.enums.Dynamic;

import static com.loserico.common.lang.utils.Assert.notNull;

/**
 * <p>
 * Copyright: (C), 2021-03-26 16:31
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticIndexMappingBuilder extends AbstractMappingBuilder {
	
	private ElasticIndexBuilder elasticIndexBuilder;
	
	public ElasticIndexMappingBuilder(ElasticIndexBuilder elasticIndexBuilder, Dynamic dynamic) {
		super(dynamic);
		notNull(elasticIndexBuilder, "ElasticIndexBuilder cannot be null");
		this.elasticIndexBuilder = elasticIndexBuilder;
	}
	
	private ElasticIndexMappingBuilder(Dynamic dynamic) {
		super(dynamic);
	}
	
	public static ElasticIndexMappingBuilder newInstance() {
		return new ElasticIndexMappingBuilder(Dynamic.TRUE);
	}
	
	public static ElasticIndexMappingBuilder newInstance(Dynamic dynamic) {
		return new ElasticIndexMappingBuilder(dynamic);
	}
	
	public ElasticIndexBuilder and() {
		return elasticIndexBuilder;
	}
	
	public boolean thenCreate() {
		return elasticIndexBuilder.create();
	}
}
