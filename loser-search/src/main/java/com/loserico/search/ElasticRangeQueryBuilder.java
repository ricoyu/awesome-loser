package com.loserico.search;

import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.builder.query.BoolRangeQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * <p>
 * Copyright: (C), 2021-06-06 14:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticRangeQueryBuilder extends BaseQueryBuilder implements BoolRangeQuery {
	
	private Object gte;
	
	private Object lte;
	
	private Object gt;
	
	private Object lt;
	
	public ElasticRangeQueryBuilder() {
	}
	
	public ElasticRangeQueryBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 返回存在该字段的文档
	 *
	 * @param field
	 * @return ElasticRangeQueryBuilder
	 */
	public ElasticRangeQueryBuilder field(String field) {
		this.field = field;
		return this;
	}
	
	public ElasticRangeQueryBuilder gte(Object gte) {
		this.gte = gte;
		return this;
	}
	
	public ElasticRangeQueryBuilder lte(Object lte) {
		this.lte = lte;
		return this;
	}
	
	public ElasticRangeQueryBuilder gt(Object gt) {
		this.gt = gt;
		return this;
	}
	
	public ElasticRangeQueryBuilder lt(Object lt) {
		this.lt = lt;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(field);
		if (gte != null) {
			rangeQueryBuilder.gte(gte);
		} else if (gt != null) {
			rangeQueryBuilder.gt(gt);
		}
		
		if (lte != null) {
			rangeQueryBuilder.lte(lte);
		} else if (lt != null) {
			rangeQueryBuilder.lt(lt);
		}
		
		return QueryBuilders.constantScoreQuery(rangeQueryBuilder);
	}

}
