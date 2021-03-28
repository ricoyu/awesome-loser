package com.loserico.search.builder;

import com.loserico.search.enums.Dynamic;

/**
 * <p>
 * Copyright: (C), 2021-03-26 16:50
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticIndexTemplateMappingBuilder extends AbstractMappingBuilder {
	
	private ElasticIndexTemplateBuilder elasticIndexTemplateBuilder;
	
	public ElasticIndexTemplateMappingBuilder(ElasticIndexTemplateBuilder elasticIndexTemplateBuilder, Dynamic dynamic) {
		super(dynamic);
		this.elasticIndexTemplateBuilder = elasticIndexTemplateBuilder;
	}
	
	@Override
	public boolean thenCreate() {
		return elasticIndexTemplateBuilder.create();
	}
}
