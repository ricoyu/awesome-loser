package com.loserico.search.builder.query;

import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2021-09-03 17:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticIdsQueryBuilder extends BaseQueryBuilder {
	
	private String[] ids;
	
	public ElasticIdsQueryBuilder(String... indices) {
		super(indices);
	}
	
	public ElasticIdsQueryBuilder ids(String... ids) {
		Objects.requireNonNull(ids, "ids cannot be null!");
		this.ids = ids;
		this.size = ids.length;
		return this;
	}
	
	public ElasticIdsQueryBuilder ids(List<String> ids) {
		Objects.requireNonNull(ids, "ids cannot be null!");
		this.ids = ids.stream().toArray(String[]::new);
		this.size = ids.size();
		return this;
	}
	
	public ElasticIdsQueryBuilder ids(Set<String> ids) {
		Objects.requireNonNull(ids, "ids cannot be null!");
		this.ids = ids.stream().toArray(String[]::new);
		this.size = ids.size();
		return this;
	}
	
	public ElasticIdsQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 * @param fields
	 * @return ElasticIdsQueryBuilder
	 */
	public ElasticIdsQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 * @param fields
	 * @return ElasticIdsQueryBuilder
	 */
	public ElasticIdsQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticIdsQueryBuilder
	 */
	public ElasticIdsQueryBuilder includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.includeSource = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticIdsQueryBuilder
	 */
	public ElasticIdsQueryBuilder excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSource = sources;
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
	 * @return ElasticIdsQueryBuilder
	 */
	public ElasticIdsQueryBuilder sort(String sort) {
		List<SortOrder> sortOrders = SortSupport.sort(sort);
		this.sortOrders.addAll(sortOrders);
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		IdsQueryBuilder queryBuilder = new IdsQueryBuilder();
		queryBuilder.addIds(ids);
		return queryBuilder;
	}
}
