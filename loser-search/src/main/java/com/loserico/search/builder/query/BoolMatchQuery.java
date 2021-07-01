package com.loserico.search.builder.query;

import org.elasticsearch.index.query.Operator;

/**
 * <p>
 * Copyright: (C), 2021-06-13 15:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface BoolMatchQuery extends BoolQuery{
	
	public MatchQuery operator(Operator operator);
	
	public MatchQuery minimumShouldMatch(int minimumShouldMatch);
	
	public MatchQuery minimumShouldMatch(String minimumShouldMatch);

}
