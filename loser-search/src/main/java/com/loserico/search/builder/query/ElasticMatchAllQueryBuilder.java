package com.loserico.search.builder.query;

import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-05-07 17:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticMatchAllQueryBuilder extends BaseQueryBuilder {
	
	public ElasticMatchAllQueryBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 设置分页属性, 深度分页建议用Search After
	 *
	 * @param from
	 * @param size
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder paging(Integer from, Integer size) {
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
	public ElasticMatchAllQueryBuilder size(int size) {
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
	public ElasticMatchAllQueryBuilder sort(String sort) {
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
	public ElasticMatchAllQueryBuilder sort(String field, Direction direction) {
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
	public ElasticMatchAllQueryBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 提升或者降低查询的权重
	 *
	 * @param boost
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder boost(float boost) {
		this.boost = boost;
		return this;
	}
	
	/**
	 * Function Score Query中用到
	 * <ul>
	 * <li/>Multiply(默认值)  算分与函数值的乘积
	 * <li/>Sum  算分与函数的和
	 * <li/>Min / Max   算分与函数取 最小/最大值
	 * <li/>Replace   使用函数值取代算分
	 * </ul>
	 *
	 * @param boostMode
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder boostMode(CombineFunction boostMode) {
		notNull(boostMode, "boostMode cannot be null!");
		this.boostMode = boostMode;
		return this;
	}
	
	public ElasticMatchAllQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 *
	 * @param constantScore
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder constantScore(boolean constantScore) {
		this.constantScore = constantScore;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchAllQueryBuilder includeSources(List<String> fields) {
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
	public ElasticMatchAllQueryBuilder excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSource = sources;
		return this;
	}
	
	/**
	 * 脚本字段查询
	 * <p>
	 * doc['email'].value 表示获取_source.email字段的值
	 *
	 * @param fieldName
	 * @param script
	 * @return ElasticMatchQueryBuilder
	 */
	@Override
	public ElasticMatchAllQueryBuilder scriptField(String fieldName, String script) {
		super.scriptField(fieldName, script);
		return this;
	}
	
	public ElasticMatchAllQueryBuilder refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
		if (constantScore) {
			return QueryBuilders.constantScoreQuery(matchAllQueryBuilder);
		}
		return matchAllQueryBuilder;
	}
}
