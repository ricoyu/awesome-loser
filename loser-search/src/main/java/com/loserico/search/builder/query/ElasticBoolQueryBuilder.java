package com.loserico.search.builder.query;

import com.loserico.search.enums.BoolQuery;
import lombok.Data;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

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
	private static class Node<K, V>{
		
		private K key;
		
		private V value;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
	
	public ElasticBoolQueryBuilder(String... indices) {
		this.indices = indices;	
	}
	
	public ElasticBoolQueryBuilder must(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(BoolQuery.MUST, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder mustNot(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(BoolQuery.MUST_NOT, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder should(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(BoolQuery.SHOULD, queryBuilder));
		return this;
	}
	
	public ElasticBoolQueryBuilder filter(QueryBuilder queryBuilder) {
		this.queryBuilders.add(new Node(BoolQuery.FILTER, queryBuilder));
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		return null;
	}
}
