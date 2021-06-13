package com.loserico.search.builder.query;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticRangeQueryBuilder;
import com.loserico.search.enums.BoolQueryType;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.loserico.search.enums.BoolQueryType.FILTER;
import static com.loserico.search.enums.BoolQueryType.MUST;
import static com.loserico.search.enums.BoolQueryType.MUST_NOT;
import static com.loserico.search.enums.BoolQueryType.SHOULD;

/**
 * <p>
 * Copyright: (C), 2021-06-06 21:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticBoolQueryBuilder extends BaseQueryBuilder {
	
	private List<Node> queryBuilders = new ArrayList<>();
	
	@Data
	private static class Node{
		
		private BoolQueryType type;
		
		private QueryBuilder builder;
		
		public Node(BoolQueryType type, QueryBuilder builder) {
			this.type = type;
			this.builder = builder;
		}
	}
	
	public ElasticBoolQueryBuilder(String... indices) {
		this.indices = indices;	
	}
	
	public ElasticBoolQueryBuilder must(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(MUST, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder mustNot(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(MUST_NOT, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder should(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(SHOULD, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder filter(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(FILTER, queryBuilder));
		return this;
	}
	
	public BoolRangeQuery range(String field) {
		ElasticRangeQueryBuilder rangeQueryBuilder = new ElasticRangeQueryBuilder();
		rangeQueryBuilder.field(field);
		ReflectionUtils.setField("boolQueryBuilder", rangeQueryBuilder, this);
		return rangeQueryBuilder;
				
	}
	
	@Override
	protected QueryBuilder builder() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (Node node : queryBuilders) {
			if (node.type == MUST) {
				boolQueryBuilder.must(node.builder);
				continue;
			}
			if (node.type == MUST_NOT) {
				boolQueryBuilder.mustNot(node.builder);
				continue;
			}
			if (node.type == SHOULD) {
				boolQueryBuilder.should(node.builder);
				continue;
			}
			if (node.type == FILTER) {
				boolQueryBuilder.filter(node.builder);
				continue;
			}
		}
		
		return boolQueryBuilder;
	}
}
