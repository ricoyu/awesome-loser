package com.loserico.search.builder.agg;

import com.loserico.search.enums.AggType;

/**
 * <p>
 * Copyright: (C), 2021-07-12 18:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticSubAggBuilder {
	
	/**
	 * 聚合名字
	 */
	private String name;
	
	/**
	 * 要对哪个字段聚合
	 */
	private String field;
	
	/**
	 * 哪一种聚合
	 */
	private AggType aggType;
	
	private ElasticSubAggBuilder(String name) {
		this.name = name;
	}
	
	public static ElasticSubAggBuilder instance(String name) {
		return new ElasticSubAggBuilder(name);
	}
}
