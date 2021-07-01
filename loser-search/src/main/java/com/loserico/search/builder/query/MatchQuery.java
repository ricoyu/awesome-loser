package com.loserico.search.builder.query;

import org.elasticsearch.index.query.Operator;

/**
 * <p>
 * Copyright: (C), 2021-06-06 21:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface MatchQuery {
	
	public MatchQuery query(String field, Object value);
	
	public MatchQuery operator(Operator operator);
	
	public MatchQuery minimumShouldMatch(int minimumShouldMatch);
	
	public MatchQuery minimumShouldMatch(String minimumShouldMatch);
}
