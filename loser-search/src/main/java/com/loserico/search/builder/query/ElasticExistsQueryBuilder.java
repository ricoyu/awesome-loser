package com.loserico.search.builder.query;

import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * <p>
 * Copyright: (C), 2021-06-06 14:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticExistsQueryBuilder extends BaseQueryBuilder {
	
	public ElasticExistsQueryBuilder(String... indices) {
		super(indices);
	}
	
	/**
	 * 返回存在该字段的文档
	 * @param field
	 * @return ElasticExistsQueryBuilder
	 */
	public ElasticExistsQueryBuilder field(String field) {
		this.field = field;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		ExistsQueryBuilder queryBuilder = QueryBuilders.existsQuery(field);
		return QueryBuilders.constantScoreQuery(queryBuilder);
	}
}
