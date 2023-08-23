package com.loserico.search.builder.query;

import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
public class ElasticMatchQueryBuilder extends BaseQueryBuilder implements MatchQuery, BoolMatchQuery {
	
	/**
	 * 如果要查询的title值是 "Last Christmas", 那么默认搜索title包含last或者christmas
	 * 如果要提高查询的精准度, 只想查到同时包含"Last Christmas"的title, 可以设置operator为AND
	 */
	protected Operator operator;
	
	/**
	 * 如果用户给定 5 个查询词项, 想查找只包含其中 4 个的文档, 该如何处理？<p>
	 * match 查询支持 minimum_should_match 最小匹配参数, 这让我们可以指定必须匹配的词项数用来表示一个文档是否相关。
	 * 我们可以将其设置为某个具体数字, 更常用的做法是将其设置为一个百分数, 因为我们无法控制用户搜索时输入的单词数量:
	 * <p>
	 * 比如我们搜索 Once Upon a Time in the Midlands<p>
	 * 如果是standard分词器, 分词后得到的词项为: "once", "upon", "a", "time", "in", "the", "midlands"<p>
	 * 所以 minimum_should_match: 7 或者 minimum_should_match: "100%" 表示这些词都要包含<p>
	 * minimum_should_match: 6 就表示文本中这个字段少一个词项的也可以搜索到
	 */
	private Object minimumShouldMatch;
	
	public ElasticMatchQueryBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 设置查询字段, 值
	 *
	 * @param field
	 * @param value
	 * @return ElasticMatchQueryBuilder
	 */
	@Override
	public ElasticMatchQueryBuilder query(String field, Object value) {
		this.field = field;
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
	@Override
	public ElasticMatchQueryBuilder operator(Operator operator) {
		this.operator = operator;
		return this;
	}
	
	/**
	 * 数字形式指定 minimum_should_match: 6 <p>
	 * <p>
	 * 如果用户给定 5 个查询词项, 想查找只包含其中 4 个的文档, 该如何处理？<p>
	 * match 查询支持 minimum_should_match 最小匹配参数, 这让我们可以指定必须匹配的词项数用来表示一个文档是否相关。
	 * 我们可以将其设置为某个具体数字, 更常用的做法是将其设置为一个百分数, 因为我们无法控制用户搜索时输入的单词数量:
	 * <p>
	 * 比如我们搜索 Once Upon a Time in the Midlands<p>
	 * 如果是standard分词器, 分词后得到的词项为: "once", "upon", "a", "time", "in", "the", "midlands"<p>
	 * 所以 minimum_should_match: 7 或者 minimum_should_match: "100%" 表示这些词都要包含<p>
	 * minimum_should_match: 6 就表示文本中这个字段少一个词项的也可以搜索到
	 *
	 * @param minimumShouldMatch
	 * @return ElasticMatchQueryBuilder
	 */
	@Override
	public ElasticMatchQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
		return this;
	}
	
	/**
	 * 字符串百分比形式指定 minimum_should_match: 50% <p>
	 * <p>
	 * 如果用户给定 5 个查询词项, 想查找只包含其中 4 个的文档, 该如何处理？<p>
	 * match 查询支持 minimum_should_match 最小匹配参数, 这让我们可以指定必须匹配的词项数用来表示一个文档是否相关。
	 * 我们可以将其设置为某个具体数字, 更常用的做法是将其设置为一个百分数, 因为我们无法控制用户搜索时输入的单词数量:
	 * <p>
	 * 比如我们搜索 Once Upon a Time in the Midlands<p>
	 * 如果是standard分词器, 分词后得到的词项为: "once", "upon", "a", "time", "in", "the", "midlands"<p>
	 * 所以 minimum_should_match: 7 或者 minimum_should_match: "100%" 表示这些词都要包含<p>
	 * minimum_should_match: 6 就表示文本中这个字段少一个词项的也可以搜索到
	 *
	 * @param minimumShouldMatch
	 * @return ElasticMatchQueryBuilder
	 */
	@Override
	public ElasticMatchQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
		return this;
	}
	
	/**
	 * 设置分页属性, 深度分页建议用Search After
	 *
	 * @param from
	 * @param size
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder paging(Integer from, Integer size) {
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
	public ElasticMatchQueryBuilder from(int from) {
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
	public ElasticMatchQueryBuilder size(int size) {
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
	public ElasticMatchQueryBuilder sort(String sort) {
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
	public ElasticMatchQueryBuilder sort(String field, Direction direction) {
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
	public ElasticMatchQueryBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 提升或者降低查询的权重
	 *
	 * @param boost
	 * @return ElasticMatchQueryBuilder
	 */
	public BoolMatchQuery boost(float boost) {
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
	public ElasticMatchQueryBuilder boostMode(CombineFunction boostMode) {
		notNull(boostMode, "boostMode cannot be null!");
		this.boostMode = boostMode;
		return this;
	}
	
	public ElasticMatchQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 *
	 * @param constantScore
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder constantScore(boolean constantScore) {
		this.constantScore = constantScore;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticMatchQueryBuilder
	 */
	public ElasticMatchQueryBuilder includeSources(List<String> fields) {
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
	public ElasticMatchQueryBuilder excludeSources(List<String> fields) {
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
	public ElasticMatchQueryBuilder scriptField(String fieldName, String script) {
		super.scriptField(fieldName, script);
		return this;
	}
	
	public ElasticMatchQueryBuilder refresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	@Override
	public ElasticMatchQueryBuilder highlightField(String highlightField) {
		super.highlightField(highlightField);
		return this;
	}
	
	@Override
	public ElasticMatchQueryBuilder storedFields(String... storedFields) {
		super.storedFields(storedFields);
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		if (isNotBlank(field)) {
			MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, value);
			if (operator != null) {
				matchQueryBuilder.operator(operator);
			}
			if (minimumShouldMatch != null) {
				matchQueryBuilder.minimumShouldMatch(minimumShouldMatch.toString());
			}
			
			if (constantScore) {
				return QueryBuilders.constantScoreQuery(matchQueryBuilder);
			}
			
			matchQueryBuilder.boost(boost);
			return matchQueryBuilder;
		}
		
		MatchAllQueryBuilder matchAllQueryBuilder = new MatchAllQueryBuilder();
		if (constantScore) {
			return QueryBuilders.constantScoreQuery(matchAllQueryBuilder);
		}
		return matchAllQueryBuilder;
	}
}
