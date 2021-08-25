package com.loserico.search.builder.agg;

import com.loserico.common.lang.context.ThreadContext;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.builder.agg.sub.SubAggregation;
import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.constants.ElasticConstants;
import com.loserico.search.enums.SortOrder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.loserico.common.lang.utils.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-05-10 11:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public abstract class AbstractAggregationBuilder{
	
	protected String[] indices;
	
	/**
	 * 聚合名字
	 */
	protected String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	protected String field;
	
	/**
	 * 要对聚合的KEY或者COUNT排序
	 */
	protected List<SortOrder> sortOrders = new ArrayList<>();
	
	///**
	// * 要对聚合的KEY还是COUNT排序
	// */
	//protected OrderBy orderBy;
	//
	///**
	// * 升序, 降序?
	// */
	//protected Direction direction;
	
	/**
	 * 聚合返回的结果中是否要包含总命中数
	 */
	protected boolean fetchTotalHits = false;
	
	protected ElasticCompositeAggregationBuilder compositeAggregationBuilder;
	
	protected BaseQueryBuilder baseQueryBuilder;
	
	/**
	 * 添加的子聚合
	 */
	protected List<SubAggregation> subAggregations = new ArrayList<>();
	
	protected void logDsl(SearchRequestBuilder builder) {
		if (log.isDebugEnabled()) {
			log.debug("Aggregation DSL:\n{}", new JSONObject(builder.toString()).toString(2));
		}
	}
	
	protected AbstractAggregationBuilder setQuery(BaseQueryBuilder queryBuilder) {
		this.baseQueryBuilder = queryBuilder;
		return this;
	}
	
	protected SearchRequestBuilder searchRequestBuilder() {
		SearchRequestBuilder searchRequestBuilder = ElasticUtils.client.prepareSearch(indices);
		if (baseQueryBuilder != null) {
			QueryBuilder queryBuilder = ReflectionUtils.invokeMethod(baseQueryBuilder, "builder");
			searchRequestBuilder.setQuery(queryBuilder);
		}
		//要搜索的index不存在时不报错
		searchRequestBuilder.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
		//查询返回的totalHits默认最大值是10000, 如果查到的数据超过10000, 那么拿到的totalHits就不准了, 加上这个配置解决这个问题
		searchRequestBuilder.setTrackTotalHits(true);
		
		return searchRequestBuilder;
	}
	
	protected void addTotalHitsToThreadLocal(SearchResponse searchResponse) {
		if (fetchTotalHits) {
			SearchHits hits = searchResponse.getHits();
			if (hits != null) {
				ThreadContext.put(ElasticConstants.TOTAL_HITS, hits.getTotalHits().value);
			}
		} else {
			ThreadContext.remove(ElasticConstants.TOTAL_HITS);
		}
	}
	
	/**
	 * 添加排序规则<p>
	 * sort格式: key:asc,count:desc,key<p>
	 * 其中最后一个key表示按聚合的key降序排(DESC)<p>
	 *
	 * @param sort
	 * @return QueryBuilder
	 */
	public AbstractAggregationBuilder sort(String sort) {
		if (isBlank(sort)) {
			return this;
		}
		
		/*
		 * 先把 key:asc,count:desc,key 这种形式的排序字符串用,拆开
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
			if (!"key".equalsIgnoreCase(field) && !"count".equalsIgnoreCase(field)) {
				log.warn("Only be sort by key or count!");
				throw new IllegalArgumentException("Only be sort by key or count!");
			}
			SortOrder sortOrder = SortOrder.fieldSort(field).direction(direction);
			this.sortOrders.add(sortOrder);
		}
		
		return this;
	}
}
