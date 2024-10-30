package com.loserico.search.builder.query;

import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * <p>
 * Copyright: (C), 2023-08-03 17:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticMultiMatchQueryBuilder extends BaseQueryBuilder implements Highlightable{
	
	public static final MultiMatchQueryBuilder.Type DEFAULT_TYPE = MultiMatchQueryBuilder.Type.BEST_FIELDS;
	
	private String[] fields;
	
	/**
	 * 如果要查询的title值是 "Last Christmas", 那么默认搜索title包含last或者christmas
	 * 如果要提高查询的精准度, 只想查到同时包含"Last Christmas"的title, 可以设置operator为AND
	 */
	protected Operator operator;
	
	private MultiMatchQueryBuilder.Type type = DEFAULT_TYPE;
	
	private Float tieBreaker;
	
	private String minimumShouldMatch;
	
	private List<MultiMatchHighlight> multiMatchHighlights;
	
	public ElasticMultiMatchQueryBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 设置查询字段, 值
	 *
	 * @param value 要查询的值
	 * @param fields 要在哪些字段上执行查询
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder query(Object value, String... fields) {
		this.fields = fields;
		this.value = value;
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
	public ElasticMultiMatchQueryBuilder operator(Operator operator) {
		this.operator = operator;
		return this;
	}
	
	
	/**
	 * Sets the type of the text query.
	 */
	public ElasticMultiMatchQueryBuilder type(MultiMatchQueryBuilder.Type type) {
		if (type == null) {
			throw new IllegalArgumentException("[multi_match] requires type to be non-null");
		}
		this.type = type;
		return this;
	}
	
	
	/**
	 * <p>Tie-Breaker for "best-match" disjunction queries (OR-Queries).
	 * The tie breaker capability allows documents that match more than one query clause
	 * (in this case on more than one field) to be scored better than documents that
	 * match only the best of the fields, without confusing this with the better case of
	 * two distinct matches in the multiple fields.</p>
	 *
	 * <p>A tie-breaker value of {@code 1.0} is interpreted as a signal to score queries as
	 * "most-match" queries where all matching query clauses are considered for scoring.</p>
	 *
	 * @see MultiMatchQueryBuilder.Type
	 */
	public ElasticMultiMatchQueryBuilder tieBreaker(float tieBreaker) {
		this.tieBreaker = tieBreaker;
		return this;
	}
	
	
	public ElasticMultiMatchQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
		return this;
	}
	
	/**
	 * <p>Tie-Breaker for "best-match" disjunction queries (OR-Queries).
	 * The tie breaker capability allows documents that match more than one query clause
	 * (in this case on more than one field) to be scored better than documents that
	 * match only the best of the fields, without confusing this with the better case of
	 * two distinct matches in the multiple fields.</p>
	 *
	 * <p>A tie-breaker value of {@code 1.0} is interpreted as a signal to score queries as
	 * "most-match" queries where all matching query clauses are considered for scoring.</p>
	 *
	 * @see MultiMatchQueryBuilder.Type
	 */
	public ElasticMultiMatchQueryBuilder tieBreaker(Float tieBreaker) {
		this.tieBreaker = tieBreaker;
		return this;
	}
	
	/**
	 * 设置分页属性, 深度分页建议用Search After
	 *
	 * @param from
	 * @param size
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder paging(Integer from, Integer size) {
		this.from = from;
		this.size = size;
		return this;
	}
	
	/**
	 * 从第几条记录开始, 第一条记录是1
	 *
	 * @param from 从第几页开始
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder from(int from) {
		this.from = from;
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
	public ElasticMultiMatchQueryBuilder size(int size) {
		this.size = size;
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
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder sort(String sort) {
		List<SortOrder> sortOrders = SortSupport.sort(sort);
		this.sortOrders.addAll(sortOrders);
		return this;
	}
	
	
	/**
	 * GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s<p>
	 * <p>
	 * 参考上面的查询, sort语法是 字段名:asc|desc
	 *
	 * @param direction
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder sort(String field, Direction direction) {
		notNull(field, "field cannot be null!");
		notNull(direction, "direction cannot be null!");
		return sort(field + ":" + direction);
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
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.includeSource = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMultiMatchQueryBuilder excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSource = sources;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		MultiMatchQueryBuilder queryBuilder = multiMatchQuery(value, fields);
		if (operator!= null) {
			queryBuilder.operator(operator);
		}
		if (type != null) {
			queryBuilder.type(type);
		}
		
		if (tieBreaker != null) {
			queryBuilder.tieBreaker(tieBreaker);
		}
		if (isNotBlank(minimumShouldMatch)) {
			queryBuilder.minimumShouldMatch(minimumShouldMatch);
		}
		return queryBuilder;
	}
	
	/**
	 * 如果设置了highlight, 那么queryForList放的是highlight列表, 而不是文档的source
	 * @param field
	 * @return
	 */
	@Override
	public MultiMatchHighlight highlight(String field) {
		MultiMatchHighlight multiMatchHighlight = new MultiMatchHighlight(this, field);
		if (this.multiMatchHighlights == null) {
			this.multiMatchHighlights = new ArrayList<>();
		}
		this.multiMatchHighlights.add(multiMatchHighlight);
		return multiMatchHighlight;
	}
	
	@Override
	public HighlightBuilder toHighlightBuilder() {
		if (this.multiMatchHighlights!= null &&this.multiMatchHighlights.size() > 0) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			for (MultiMatchHighlight highlight : multiMatchHighlights) {
				HighlightBuilder.Field field = highlight.toHighlightField();
				field.preTags(highlight.preTags());
				field.postTags(highlight.postTags());
				highlightBuilder.field(field);
			}
			return highlightBuilder;
		}
		return null;
	}
}
