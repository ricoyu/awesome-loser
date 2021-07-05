package com.loserico.search.builder.agg;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.builder.query.BaseQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.json.JSONObject;

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
public abstract class AbstractAggregationBuilder {
	
	protected String[] indices;
	
	/**
	 * 聚合名字
	 */
	protected String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	protected String field;
	
	protected ElasticCompositeAggregationBuilder compositeAggregationBuilder;
	
	protected BaseQueryBuilder baseQueryBuilder;
	
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
		
		return searchRequestBuilder;
	}
}
