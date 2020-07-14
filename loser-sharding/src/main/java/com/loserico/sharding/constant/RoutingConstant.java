package com.loserico.sharding.constant;

/**
 * <p>
 * Copyright: (C), 2020/2/14 18:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RoutingConstant {
	
	/**
	 * 多库 多表策略
	 */
	public static final String ROUTING_DS_TABLE_STATEGY = "ROUTING_DS_TABLE_STATEGY";
	
	/**
	 * 多库 一表策略
	 */
	public static final String ROUTGING_DS_STATEGY = "ROUTGING_DS_STATEGY";
	
	/**
	 * 一库多表策略
	 */
	public static final String ROUTGIN_TABLE_STATEGY = "ROUTGIN_TABLE_STATEGY";
	
	public static final String DEFAULT_ROUTING_FIELD = "orderId";
}
