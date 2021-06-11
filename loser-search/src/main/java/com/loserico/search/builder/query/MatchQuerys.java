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
public interface MatchQuerys {
	
	public MatchQuerys query(String field, Object value);
	
	public MatchQuerys operator(Operator operator);
	
	public MatchQuerys minimumShouldMatch(int minimumShouldMatch);
	
	public MatchQuerys minimumShouldMatch(String minimumShouldMatch);
}
