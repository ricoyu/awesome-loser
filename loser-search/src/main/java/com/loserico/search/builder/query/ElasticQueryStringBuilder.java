package com.loserico.search.builder.query;

import com.loserico.search.enums.Direction;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import static com.loserico.common.lang.utils.Assert.notEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * ES Query String Query 语法支持
 * <p>
 * Copyright: (C), 2021-04-28 17:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticQueryStringBuilder extends BaseQueryBuilder {
	
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
	 */
	private String defaultField;
	
	/**
	 * 指定查询的字段
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
	 */
	private String[] fields;
	
	/**
	 * 查询语句, 如:
	 * <ol>
	 *     <li/>name:ruan 查name字段包含ruan
	 *     <li/>name:(+yiming -ruan)
	 *     <li/>(ruan AND yiming) OR (Java AND Elasticsearch)
	 *     <li/>"name:\"ruan yiming\"" 查询name包含"ruan yiming"这个短语
	 * </ol>
	 * 
	 * 也可以为不同字段指定不同的查询条件
	 * <pre> {@code
	 * GET /_search
	 * {
	 *   "query": {
	 *     "query_string": {
	 *       "query": "(content:this OR name:this) AND (content:that OR name:that)"
	 *     }
	 *   }
	 * }
	 * }</pre>
	 */
	private String queryString;
	
	/**
	 * 如果要查询的title值是 "Last Christmas", 那么默认搜索title包含last或者christmas
	 * 如果要提高查询的精准度, 只想查到同时包含"Last Christmas"的title, 可以设置operator为AND
	 */
	protected Operator operator;
	
	public ElasticQueryStringBuilder(String... indices) {
		notNull(indices, "indices cannot be null!");
		this.indices = indices;
	}
	
	public ElasticQueryStringBuilder query(String queryString) {
		notEmpty(queryString, "queryString cannot be empty");
		this.queryString = queryString;
		return this;
	}
	
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
	public ElasticQueryStringBuilder fields(String... fields) {
		this.fields = fields;
		return this;
	}
	
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
	public ElasticQueryStringBuilder defaultField(String defaultField) {
		this.defaultField = defaultField;
		return this;
	}
	
	/**
	 * 如果要查询的title值是 "Last Christmas", 那么默认搜索title包含last或者christmas<p>
	 * 如果要提高查询的精准度, 只想查到同时包含"Last Christmas"的title, 可以设置operator为AND<p>
	 * <p>
	 * 注意只有Match Query可以设置Operator, Term Query不可以, 因为后者是精确匹配<p>
	 *
	 * @param operator
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticQueryStringBuilder operator(Operator operator) {
		this.operator = operator;
		return this;
	}
	
	/**
	 * 添加排序规则<p>
	 * sort格式: 字段1:asc,字段2:desc,字段3<p>
	 * 其中字段3按升序排(ASC)<p>
	 * <p>
	 * 注意: text类型字段不能排序, 要用field
	 *
	 * @param sort
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder sort(String sort) {
		super.sort(sort);
		return this;
	}
	
	/**
	 * GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s<p>
	 * <p>
	 * 参考上面的查询, sort语法是 字段名:asc|desc
	 *
	 * @param direction
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder sort(String field, Direction direction) {
		notNull(field, "field cannot be null!");
		notNull(direction, "direction cannot be null!");
		return sort(field + ":" + direction);
	}
	
	/**
	 * 设置分页属性, 深度分页建议用Search After
	 *
	 * @param from
	 * @param size
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder paging(int from, int size) {
		this.from = from;
		this.size = size;
		return this;
	}
	
	/**
	 * ES默认只返回10条数据, 这里可以指定返回多少条数据<p>
	 * 通过Search After分页时第一次需要设置size<p>
	 * 深度分页时推荐用Search After
	 *
	 * @param size
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticQueryStringBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 避免深度分页的性能问题, 可以实时获取下一页文档信息<p>
	 * 第一步搜索需要指定sort, 并且保证值是唯一的(可以通过加入_id保证唯一性)<p>
	 * 然后使用上一次, 最后一个文档的sort值进行查询<p>
	 * 这个就是查询得到的最后一个文档的sort值
	 * <p>
	 * 注意设置了searchAfter就不要设置from了, 只要指定size以及排序就可以了
	 *
	 * @param searchAfter
	 * @return ElasticQueryBuilder
	 */
	public ElasticQueryStringBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return QueryBuilder
	 */
	public ElasticQueryStringBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source, 可以使用通配符
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source, 可以使用通配符
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticQueryStringBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 脚本字段查询
	 *
	 * doc['email'].value 表示获取_source.email字段的值
	 *
	 * @param fieldName
	 * @param script
	 * @return ElasticMatchQueryBuilder
	 */
	@Override
	public ElasticQueryStringBuilder scriptField(String fieldName, String script) {
		super.scriptField(fieldName, script);
		return this;
	}
	
	/**
	 * 查询返回的结果类型
	 *
	 * @param resultType
	 * @return UriQueryBuilder
	 */
	public ElasticQueryStringBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		if (isBlank(queryString)) {
			throw new IllegalArgumentException("Please set query string first!");
		}
		QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(queryString);
		
		if (isNotBlank(defaultField)) {
			builder.defaultField(defaultField);
		}
		
		//设置要在哪些字段上查询
		if (fields != null && fields.length > 0) {
			for (int i = 0; i < fields.length; i++) {
				builder.field(fields[i]);
			}
		}
		
		if (operator != null) {
			builder.defaultOperator(operator);
		}
		
		return builder;
	}
	
}
