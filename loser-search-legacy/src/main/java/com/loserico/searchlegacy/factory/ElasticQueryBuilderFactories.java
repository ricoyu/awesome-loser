package com.loserico.searchlegacy.factory;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import static com.loserico.common.lang.utils.Assert.notNull;

/**
 * <p>
 * Copyright: (C), 2021-03-26 11:33
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ElasticQueryBuilderFactories {
	
	public TermQueryBuilder termQuery(String name, Object value) {
		notNull(name, "name cannot be null!");
		if (value == null) {
			return QueryBuilders.termQuery(name, value);
		}
		
		if (value instanceof String) {
			return QueryBuilders.termQuery(name, (String)value);
		}
		
		return null;
	}
}
