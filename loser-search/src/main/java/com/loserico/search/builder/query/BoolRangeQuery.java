package com.loserico.search.builder.query;

/**
 * <p>
 * Copyright: (C), 2021-06-11 17:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface BoolRangeQuery extends BoolQuery{
	
	
	/**
	 * 返回存在该字段的文档
	 *
	 * @param field
	 * @return ElasticRangeQueryBuilder
	 */
	public BoolRangeQuery field(String field);
	
	public BoolRangeQuery gte(Object gte);
	
	public BoolRangeQuery lte(Object lte);
	
	public BoolRangeQuery gt(Object gt);
	
	public BoolRangeQuery lt(Object lt);
	
}
