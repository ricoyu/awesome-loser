package com.loserico.search.builder.agg;

import com.loserico.common.lang.context.ThreadContext;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.builder.agg.sub.SubAggregation;
import com.loserico.search.builder.query.BaseQueryBuilder;
import com.loserico.search.constants.ElasticConstants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
		SearchRequestBuilder searchRequestBuilder = ElasticUtils.CLIENT.prepareSearch(indices);
		if (baseQueryBuilder != null) {
			QueryBuilder queryBuilder = ReflectionUtils.invokeMethod("builder", baseQueryBuilder);
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
}
