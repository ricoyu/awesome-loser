package com.loserico.cloud.sentinel.enums;

/**
 * <p>
 * Copyright: (C), 2022-08-25 19:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum BlockType {
	
	/**
	 * 流控规则限流
	 */
	FLOW,
	
	/**
	 * 熔断
	 */
	DEGRADE,
	
	/**
	 * 热点参数
	 */
	HOT_PARAM,
	
	/**
	 * 系统规则
	 */
	SYSTEM,
	
	/**
	 * 授权规则
	 */
	AUTH,
	
	/**
	 * 集群流控
	 */
	CLUSTER
}
