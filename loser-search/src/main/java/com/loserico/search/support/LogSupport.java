package com.loserico.search.support;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.json.JSONObject;

/**
 * <p>
 * Copyright: (C), 2021-04-29 11:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class LogSupport {
	
	/**
	 * 打印QueryDSL
	 * @param builder
	 */
	public static void logQueryDsl(QueryBuilder builder) {
		if (log.isDebugEnabled()) {
			log.debug("Query DSL:\n{}", new JSONObject(builder.toString()).toString(2));
		}
	}
}
