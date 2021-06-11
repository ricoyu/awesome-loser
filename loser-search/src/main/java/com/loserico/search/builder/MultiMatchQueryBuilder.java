package com.loserico.search.builder;

import com.loserico.search.builder.query.BaseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Multi Match Query 跨字段搜索
 * 
 * <p>
 * Copyright: (C), 2021-06-09 15:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MultiMatchQueryBuilder extends BaseQueryBuilder {
	
	@Override
	protected QueryBuilder builder() {
		return null;
	}
}
