package com.loserico.search.builder.query;

/**
 * <p>
 * Copyright: (C), 2021-06-13 15:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface BoolQuery {
	
	public ElasticBoolQueryBuilder must();
	
	public ElasticBoolQueryBuilder mustNot();
	
	public ElasticBoolQueryBuilder should();
	
	public ElasticBoolQueryBuilder filter();
}
