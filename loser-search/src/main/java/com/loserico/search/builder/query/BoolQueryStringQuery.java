package com.loserico.search.builder.query;

import org.elasticsearch.index.query.Operator;

/**
 * <p>
 * Copyright: (C), 2021-08-30 17:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface BoolQueryStringQuery extends BoolQuery {
	
	/**
	 * 指定要在哪些字段上查询
	 * <pre>
	 * POST users/_search
	 * {
	 *   "query": {
	 *     "query_string": {
	 *       "fields": ["name", "about"],
	 *       "query": "(ruan AND yiming) OR (Java AND Elasticsearch)"
	 *     }
	 *   }
	 * }
	 * </pre>
	 *
	 * @param fields
	 * @return UriQueryBuilder
	 */
	public ElasticQueryStringBuilder fields(String... fields);
	
	/**
	 * 指定查询的字段
	 * <pre>
	 * POST users/_search
	 * {
	 *   "query": {
	 *     "query_string": {
	 *       "default_field": "name",
	 *       "query": "ruan AND yiming"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * @param defaultField
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder defaultField(String defaultField);
	
	/**
	 * 如果要查询的title值是 "Last Christmas", 那么默认搜索title包含last或者christmas<p>
	 * 如果要提高查询的精准度, 只想查到同时包含"Last Christmas"的title, 可以设置operator为AND<p>
	 * <p>
	 * 注意只有Match Query可以设置Operator, Term Query不可以, 因为后者是精确匹配<p>
	 *
	 * @param operator
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticQueryStringBuilder operator(Operator operator);
}
