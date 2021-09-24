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
	
	/**
	 * 条件必须满足, 且计入算分
	 * @return ElasticBoolQueryBuilder
	 */
	public ElasticBoolQueryBuilder must();
	
	/**
	 * 条件必须不满足, 不计入算分
	 * @return ElasticBoolQueryBuilder
	 */
	public ElasticBoolQueryBuilder mustNot();
	
	public ElasticBoolQueryBuilder should();
	
	/**
	 * 条件必须满足, 不计入算分
	 * @return ElasticBoolQueryBuilder
	 */
	public ElasticBoolQueryBuilder filter();
}
