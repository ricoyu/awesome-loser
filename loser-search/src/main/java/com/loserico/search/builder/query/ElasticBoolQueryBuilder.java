package com.loserico.search.builder.query;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticRangeQueryBuilder;
import com.loserico.search.enums.BoolQueryType;
import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.SortSupport;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public BoolTermQuery term(String field, Object value) {
		ElasticTermQueryBuilder termQueryBuilder = new ElasticTermQueryBuilder();
		termQueryBuilder.query(field, value);
		ReflectionUtils.setField("boolQueryBuilder", termQueryBuilder, this);
		return termQueryBuilder;
	}
	
	/**
	 * terms query
	 * @param field  要对哪个字段聚合
	 * @param values 可以是 "val1", "val2" 数组形式; 也可以是一个List类型
	 * @return BoolQuery
	 */
	public BoolQuery terms(String field, Object... values) {
		if (values != null && values.length == 1) {
			if (values[0] instanceof Collection) {
				values = ((Collection)values[0]).stream().toArray(Object[]::new);
			}
		}
		ElasticTermsQueryBuilder termsQueryBuilder = new ElasticTermsQueryBuilder();
		termsQueryBuilder.query(field, values);
		ReflectionUtils.setField("boolQueryBuilder", termsQueryBuilder, this);
		return termsQueryBuilder;
	}
	
	public BoolMatchQuery match(String field, String value) {
		ElasticMatchQueryBuilder matchQueryBuilder = new ElasticMatchQueryBuilder();
		matchQueryBuilder.query(field, value);
		ReflectionUtils.setField("boolQueryBuilder", matchQueryBuilder, this);
		return matchQueryBuilder;
	}
	
	public BoolRangeQuery range(String field) {
		ElasticRangeQueryBuilder rangeQueryBuilder = new ElasticRangeQueryBuilder();
		rangeQueryBuilder.field(field);
		ReflectionUtils.setField("boolQueryBuilder", rangeQueryBuilder, this);
		return rangeQueryBuilder;
	}
	
	public BoolQuery exists(String field) {
		ElasticExistsQueryBuilder existsQueryBuilder = new ElasticExistsQueryBuilder();
		existsQueryBuilder.field(field);
		ReflectionUtils.setField("boolQueryBuilder", existsQueryBuilder, this);
		return existsQueryBuilder;
	}
	
	public ElasticQueryStringBuilder queryString(String queryString) {
		ElasticQueryStringBuilder queryStringBuilder = new ElasticQueryStringBuilder();
		queryStringBuilder.query(queryString);
		ReflectionUtils.setField("boolQueryBuilder", queryStringBuilder, this);
		return queryStringBuilder;
	}
	
	public ElasticIdsQueryBuilder ids(String... ids) {
		ElasticIdsQueryBuilder elasticIdsQueryBuilder = new ElasticIdsQueryBuilder();
		elasticIdsQueryBuilder.ids(ids);
		ReflectionUtils.setField("boolQueryBuilder", elasticIdsQueryBuilder, this);
		return elasticIdsQueryBuilder;
	}
	
	public ElasticIdsQueryBuilder ids(List<String> ids) {
		ElasticIdsQueryBuilder elasticIdsQueryBuilder = new ElasticIdsQueryBuilder();
		elasticIdsQueryBuilder.ids(ids.stream().toArray(String[]::new));
		ReflectionUtils.setField("boolQueryBuilder", elasticIdsQueryBuilder, this);
		return elasticIdsQueryBuilder;
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
	public ElasticBoolQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
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
	public ElasticBoolQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
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
	public ElasticBoolQueryBuilder paging(Integer from, Integer size) {
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
		public ElasticBoolQueryBuilder size(int size) {
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
	 * @return QueryBuilder
	 */
	public ElasticBoolQueryBuilder sort(String sort) {
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
	 * @return UriQueryBuilder
	 */
	public ElasticBoolQueryBuilder sort(String field, Direction direction) {
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
	 * @return ElasticQueryBuilder
	 */
	public ElasticBoolQueryBuilder searchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return QueryBuilder
	 */
	public ElasticBoolQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticBoolQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticBoolQueryBuilder includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.includeSource = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticBoolQueryBuilder excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSource = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public ElasticBoolQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 提升或者降低查询的权重
	 *
	 * @param boost
	 * @return QueryBuilder
	 */
	public ElasticBoolQueryBuilder boost(float boost) {
		this.boost = boost;
		return this;
	}
	
	public ElasticBoolQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	@Override
	protected QueryBuilder builder() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		queryBuilders.sort((prev, next) -> prev.type.compareTo(next.type));
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
