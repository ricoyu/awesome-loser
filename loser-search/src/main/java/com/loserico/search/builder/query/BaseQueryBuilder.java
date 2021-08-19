package com.loserico.search.builder.query;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.enums.SortOrder.SortOrderBuilder;
import com.loserico.search.support.SearchHitsSupport;
import com.loserico.search.vo.ElasticPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.common.lang.utils.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.elasticsearch.common.lucene.search.function.CombineFunction.MULTIPLY;

/**
 * <p>
 * Copyright: (C), 2021-04-30 11:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public abstract class BaseQueryBuilder implements BoolQuery {
	
	private ElasticBoolQueryBuilder boolQueryBuilder;
	
	/**
	 * 要查询的索引
	 */
	protected String[] indices;
	
	/**
	 * 要查询的字段
	 */
	protected String field;
	
	/**
	 * 要查询的值
	 */
	protected Object value;
	
	/**
	 * 是否要获取_source
	 */
	protected boolean fetchSource = true;
	
	/**
	 * 设置 _source 属性, 即指定要返回哪些字段, 而不是返回整个_source
	 */
	protected String[] includeSource;
	
	/**
	 * 指定查询要排除哪些字段
	 */
	protected String[] excludeSource;
	
	/**
	 * 分页相关, 起始位置
	 */
	protected Integer from;
	
	/**
	 * 分页相关, 每页大小
	 */
	protected Integer size;
	
	/**
	 * 排序 ASC DESC
	 */
	protected List<SortOrder> sortOrders = new ArrayList<>();
	
	/**
	 * 提升或者降低查询的权重
	 */
	protected float boost = 1f;
	
	/**
	 * 查询返回的结果类型
	 */
	protected Class resultType;
	
	/**
	 * 脚本字段查询
	 */
	protected Map<String, String> scriptFields = new HashMap<>();
	
	/**
	 * 是否要将Query转为constant_scre query, 以避免算分, 提高查询性能
	 * 适用于有时候我们根本不关心 TF/IDF, 只想知道一个词是否在某个字段中出现过。
	 */
	protected boolean constantScore = false;
	
	/**
	 * Elasticsearch 默认会以文档的相关度算分进行排序<br/>
	 * 可以通过指定一个或多个字段进行排序<br/>
	 * 使用相关度算分(Score)排序, 不能满足某些特定的条件: 无法针对相关度, 对排序实现更多的控制<br/>
	 * <p/>
	 * 可以在查询结束后, 对每一个匹配的文档进行一系列的重新算分, 根据新生成的分数进行排序<br/>
	 * 提供了几种默认的计算分值的函数:
	 * <ul>
	 * <li/>Weight  为每一个文档设置一个简单而不被规范化的权重
	 * <li/>Field Value Factor 使用该数值来修改_score, 例如将"热度"和"点赞数"作为算分的参考因素
	 * <li/>Random Score 为每一个用户使用一个不同的, 随机算分结果
	 * <li/>衰减函数 以某个字段的值为标准, 距离某个值越近, 得分越高
	 * <li/>Script Score 自定义脚本完全控制所需逻辑
	 * </ul>
	 */
	protected ScoreFunctionBuilder scoreFunctionBuilder;
	
	protected CombineFunction boostMode = MULTIPLY;
	
	/**
	 * 避免深度分页的性能问题, 可以实时获取下一页文档信息<p>
	 * 第一步搜索需要指定sort, 并且保证值是唯一的(可以通过加入_id保证唯一性)<p>
	 * 然后使用上一次, 最后一个文档的sort值进行查询<p>
	 * 这个就是查询得到的最后一个文档的sort值
	 */
	protected Object[] searchAfter;
	
	public BaseQueryBuilder(String... indices) {
		notNull(indices, "indices cannot be null!");
		this.indices = indices;
	}
	
	public BaseQueryBuilder(String field, Object value) {
		notNull(field, "field cannot be null!");
		this.field = field;
		this.value = value;
	}
	
	/**
	 * 添加基于字段的排序, 默认升序
	 *
	 * @param direction
	 * @return ElasticQueryBuilder
	 */
	public BaseQueryBuilder scoreSort(Direction direction) {
		SortOrder sortOrder = null;
		SortOrderBuilder sortOrderBuilder = SortOrder.scoreSort();
		if (direction == null || direction == Direction.ASC) {
			sortOrder = sortOrderBuilder.asc();
		} else {
			sortOrder = sortOrderBuilder.desc();
		}
		
		sortOrders.add(sortOrder);
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
	public BaseQueryBuilder sort(String sort) {
		if (isBlank(sort)) {
			return this;
		}
		
		/*
		 * 先把 name,age:asc,year:desc 这种形式的排序字符串用,拆开
		 */
		String[] sorts = split(sort, ",");
		for (int i = 0; i < sorts.length; i++) {
			String s = sorts[i];
			if (isBlank(s)) {
				log.warn("you entered a block sort!");
				continue;
			}
			
			//再用冒号拆出 field 和 asc|desc
			String[] fieldAndDirection = s.trim().split(":");
			//field:asc 拆开来长度只能是1或者2
			if (fieldAndDirection.length == 0) {
				log.warn("wrong sort gramma {}, should be of type field1:asc,field2:desc", sort);
				continue;
			}
			
			//数组第一个元素是字段名, 不能省略, 所以为空的话表示语法不正确
			String field = fieldAndDirection[0].trim();
			if (isBlank(field)) {
				log.warn("field is blank, skip");
				continue;
			}
			
			String direction = null;
			if (fieldAndDirection.length > 1) {
				direction = fieldAndDirection[1].trim();
			}
			//排序规则为空的话, 看下字段名是不是-开头的, 如果是的话, 降序, 否则默认升序
			if (isBlank(direction)) {
				if (field.startsWith("-")) {
					field = field.substring(1);
					direction = "desc";
				} else {
					direction = "asc";
				}
			}
			SortOrder sortOrder = SortOrder.fieldSort(field).direction(direction);
			this.sortOrders.add(sortOrder);
		}
		
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public BaseQueryBuilder includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.includeSource = sources;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public BaseQueryBuilder includeSources(String... fields) {
		this.includeSource = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return QueryStringBuilder
	 */
	public BaseQueryBuilder excludeSources(List<String> fields) {
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
	public BaseQueryBuilder excludeSources(String... fields) {
		this.excludeSource = fields;
		return this;
	}
	
	/**
	 * 脚本字段查询
	 *
	 * @param fieldName
	 * @param script
	 * @return BaseQueryBuilder
	 */
	protected BaseQueryBuilder scriptField(String fieldName, String script) {
		this.scriptFields.put(fieldName, script);
		return this;
	}
	
	protected abstract QueryBuilder builder();
	
	/**
	 * 执行查询
	 *
	 * @param <T>
	 * @return List<T>
	 */
	public <T> List<T> queryForList() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return Collections.emptyList();
		}
		
		return SearchHitsSupport.toList(hits, resultType);
	}
	
	/**
	 * 执行查询并返回script_fields指定的字段
	 *
	 * @return Map<String, List < Object>>
	 */
	public Map<String, List<Object>> queryForScriptFields() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return Collections.emptyMap();
		}
		
		return SearchHitsSupport.toScriptFieldsMap(hits);
	}
	
	/**
	 * 执行Search After分页查询<p>
	 * 需要带上上一次查询拿到的最后一个文档的sort值进行查询<p>
	 * <p>
	 * 什么是上一次查询得到的sort值?<p>
	 * <p>
	 * 我们来看下面这个Query DSL, 先按年龄倒序排, 为了避免有相同年龄的user导致排序不唯一, 加入第二个排序规则_id asc
	 * <pre>
	 * POST users/_search
	 * {
	 *   "size": 10,
	 *   "query": {"match_all": {}},
	 *   "sort": [
	 *     {"age": "desc"},
	 *     {"_id": "asc"}
	 *   ]
	 * }
	 * </pre>
	 * <p>
	 * 在取返回的数据本身之外, 把最后一个文档的sort值也拿回来, 作为下一次查询的sort
	 * <p>
	 * 返回的结果片段如下
	 * <pre>
	 * "hits" : {
	 *   "total" : {
	 *     ...
	 *   },
	 *   "hits" : [
	 *     {
	 *       "_id" : "IPzNyXcBE7I_Lg26stqp",
	 *       "_source" : {
	 *         "name" : "user4",
	 *         "age" : 13
	 *       },
	 *       "sort" : [
	 *         13,
	 *         "IPzNyXcBE7I_Lg26stqp"
	 *       ]
	 *     }
	 *   ]
	 * }
	 * </pre>
	 * <p>
	 * 所以, 这里的参数sort传的就是上一次返回的sort值, 如
	 * <pre>
	 * [
	 *   13,
	 *   "IPzNyXcBE7I_Lg26stqp"
	 * ]
	 * </pre>
	 *
	 * @param <T>
	 * @return List<T>
	 */
	public <T> ElasticPage<T> queryForPage() {
		SearchResponse response = searchResponse();
		Long total = response.getHits().getTotalHits().value;
		SearchHit[] hits = response.getHits().getHits();
		
		if (hits.length == 0) {
			return ElasticPage.emptyResult();
		}
		
		//拿到本次的sort
		Object[] sortValues = SearchHitsSupport.sortValues(hits);
		//本次查询得到结果集
		List<T> results = SearchHitsSupport.toList(hits, resultType);
		
		ElasticPage<T> elasticPage = ElasticPage.<T>builder()
				.results(results)
				.sort(sortValues)
				.build();
		if (size != null) {
			elasticPage.setPageSize(size);
		}
		if (from != null) {
			elasticPage.setCurrentPage(from);
		}
		elasticPage.setTotalCount(total.intValue());
		return elasticPage;
	}
	
	/**
	 * 执行查询, 返回一条记录
	 *
	 * @param <T>
	 * @return T
	 */
	public <T> T queryForOne() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return null;
		}
		
		SearchHit hit = hits[0];
		String source = hit.getSourceAsString();
		
		//如果没有source, 就不用管是否要封装成POJO了
		if (source == null) {
			return null;
		}
		
		//如果要封装成POJO对象
		if (ReflectionUtils.isPojo(resultType)) {
			return (T) JacksonUtils.toObject(source, resultType);
		}
		
		return (T) source;
	}
	
	/**
	 * 执行查询, 返回一个文档ID
	 *
	 * @return String
	 */
	public String queryForId() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return null;
		}
		
		SearchHit hit = hits[0];
		return hit.getId();
	}
	
	/**
	 * 执行查询, 返回文档ID列表
	 *
	 * @return List<String
	 */
	public List<String> queryForIds() {
		SearchHit[] hits = searchHits();
		
		if (hits.length == 0) {
			return Collections.emptyList();
		}
		
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < hits.length; i++) {
			ids.add(hits[i].getId());
		}
		
		return ids;
	}
	
	/**
	 * 返回查询到的记录数, 查询不会真正返回文档
	 *
	 * @return
	 */
	public long queryForCount() {
		SearchRequestBuilder builder = ElasticUtils.client.prepareSearch(indices)
				.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN) //要搜索的index不存在时不报错
				.setTrackTotalHits(true) //查询返回的totalHits默认最大值是10000, 如果查到的数据超过10000, 那么拿到的totalHits就不准了, 加上这个配置解决这个问题
				.setQuery(builder())
				.setSize(0) //count 不需要真正返回数据
				.setFetchSource(false);
		
		SearchResponse response = builder.get();
		TotalHits totalHits = response.getHits().getTotalHits();
		if (totalHits == null) {
			return 0L;
		}
		
		return totalHits.value;
	}
	
	/**
	 * 根据查询条件来删除
	 * @return long 删除的文档数量
	 */
	public long delete() {
		BulkByScrollResponse response = new DeleteByQueryRequestBuilder(ElasticUtils.client, DeleteByQueryAction.INSTANCE)
				.filter(builder())
				.source(indices)
				.get();
		return response.getDeleted();
	}
	
	/**
	 * 作为查询的公共部分抽取出来
	 *
	 * @return
	 */
	private SearchHit[] searchHits() {
		SearchResponse response = searchResponse();
		SearchHits searchHits = response.getHits();
		return searchHits.getHits();
	}
	
	private SearchResponse searchResponse() {
		SearchRequestBuilder searchRequestBuilder = ElasticUtils.client.prepareSearch(indices)
				.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN) //要搜索的index不存在时不报错
				.setTrackTotalHits(true) //查询返回的totalHits默认最大值是10000, 如果查到的数据超过10000, 那么拿到的totalHits就不准了, 加上这个配置解决这个问题
				.setQuery(builder());
		
		sortOrders.forEach(sortOrder -> sortOrder.addTo(searchRequestBuilder));
		
		/*
		 * Search After 避免深度分页, 如果用search after, 不需要指定from了
		 */
		if (searchAfter != null && searchAfter.length > 0) {
			searchRequestBuilder.searchAfter(searchAfter);
		} else {
			Integer firstResult = getFirstResult();
			if (firstResult != null) {
				searchRequestBuilder.setFrom(firstResult);
			}
		}
		
		if (size != null) {
			searchRequestBuilder.setSize(size);
		}
		
		if (includeSource != null && includeSource.length > 0 || excludeSource != null && excludeSource.length > 0) {
			searchRequestBuilder.setFetchSource(includeSource, excludeSource);
		} else {
			searchRequestBuilder.setFetchSource(fetchSource);
		}
		
		if (!scriptFields.isEmpty()) {
			for (Map.Entry<String, String> entry : scriptFields.entrySet()) {
				searchRequestBuilder.addScriptField(entry.getKey(), new Script(entry.getValue()));
			}
			/*
			 * 设置了script_fields后, 对应的fields是单独返回的, 不是放在_source里面的
			 * 所以_source就不要取了
			 */
			searchRequestBuilder.setFetchSource(false);
		}
		if (log.isDebugEnabled()) {
			log.debug("Query DSL:\n{}", new JSONObject(searchRequestBuilder.toString()).toString(2));
		}
		return searchRequestBuilder.get();
	}
	
	protected static void notNull(Object obj, String msg) {
		if (obj == null) {
			throw new IllegalArgumentException(msg);
		}
	}
	
	
	@Override
	public ElasticBoolQueryBuilder must() {
		boolQueryBuilder.must(builder());
		return boolQueryBuilder;
	}
	
	@Override
	public ElasticBoolQueryBuilder mustNot() {
		boolQueryBuilder.mustNot(builder());
		return boolQueryBuilder;
	}
	
	@Override
	public ElasticBoolQueryBuilder should() {
		boolQueryBuilder.should(builder());
		return boolQueryBuilder;
	}
	
	@Override
	public ElasticBoolQueryBuilder filter() {
		boolQueryBuilder.filter(builder());
		return boolQueryBuilder;
	}
	
	protected void logDsl(SearchRequestBuilder builder) {
		if (log.isDebugEnabled()) {
			log.debug("Aggregation DSL:\n{}", new JSONObject(builder.toString()).toString(2));
		}
	}
	
	/**
	 * 客户端API传入的from是指页码, 从1开始 <br/>
	 * ES中分页时, from是指从第几条数据开始, 第一条是0 <br/>
	 * 所以这里要换算一下, 把页码换算成第几条数据
	 *
	 * @return Integer
	 */
	public Integer getFirstResult() {
		//使用search after时, 不需要传入from
		if (from != null && from > 0) {
			int pageSize = size == null ? 10 : size;
			return (from - 1) * pageSize;
		}
		
		return null;
	}
}
