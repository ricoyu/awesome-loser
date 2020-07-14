package com.loserico.sharding.enumeration;

/**
 * 路由规则
 * <p>
 * Copyright: Copyright (c) 2020-02-14 18:40
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum RoutingStategy {
	
	/**
	 * 多库 多表策略
	 */
	ROUTING_DS_TABLE_STATEGY,
	
	/**
	 * 多库 一表策略
	 */
	ROUTGING_DS_STATEGY,
	
	/**
	 * 一库多表策略
	 */
	ROUTGIN_TABLE_STATEGY;
}