package com.loserico.search.builder.query;

import com.loserico.search.enums.Direction;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

/**
 * Terms Query: <p>
 * ES 不会对你输入的条件做任何的分词处理<p>
 * 跟term query是一样的, 只是可以搜索字段匹配多个值
 * <p>
 * Copyright: (C), 2021-04-30 11:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticTermsQueryBuilder extends BaseQueryBuilder {
	
	private Object[] values;
	
	public ElasticTermsQueryBuilder(String... indices) {
		super(indices);
	}
	
	/**
	 * 设置查询字段, 值
	 * @param field
	 * @param values
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder query(String field, Object... values) {
		this.field = field;
		this.values = values;
		return this;
	}
	
	/**
	 * 设置分页属性, 深度分页建议用Search After
	 *
	 * @param from
	 * @param size
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder paging(int from, int size) {
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
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder size(int size) {
		this.size = size;
		return this;
	}
	
	/**
	 * 添加基于字段的排序, 默认升序
	 *
	 * @param direction
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder scoreSort(Direction direction) {
		super.scoreSort(direction);
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
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 提升或者降低查询的权重
	 *
	 * @param boost
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder boost(float boost) {
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
	 * @return
	 */
	public ElasticTermsQueryBuilder boostMode(CombineFunction boostMode) {
		notNull(boostMode, "boostMode cannot be null!");
		this.boostMode = boostMode;
		return this;
	}
	
	public ElasticTermsQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 *
	 * @param constantScore
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder constantScore(boolean constantScore) {
		this.constantScore = constantScore;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 * @param fields
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 * @param fields
	 * @return ElasticTermsQueryBuilder
	 */
	public ElasticTermsQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder(field, values);
		if (constantScore) {
			return QueryBuilders.constantScoreQuery(termsQueryBuilder);
		}
		
		return termsQueryBuilder;
	}
}
