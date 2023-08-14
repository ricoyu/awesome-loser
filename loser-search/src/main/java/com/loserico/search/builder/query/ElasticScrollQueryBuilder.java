package com.loserico.search.builder.query;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * <p>
 * Copyright: (C), 2023-08-12 11:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticScrollQueryBuilder extends BaseQueryBuilder {
	
	public ElasticScrollQueryBuilder(String... indices) {
		this.indices = indices;
	}
	public ElasticScrollQueryBuilder scrollId(String scrollId) {
		this.scrollId = scrollId;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		return null;
	}
}
