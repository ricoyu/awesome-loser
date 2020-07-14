package com.loserico.sharding.core;

import com.loserico.sharding.dynamic.DataSourceHolder;
import com.loserico.sharding.exception.RoutingFieldException;
import com.loserico.sharding.exception.RoutingStategyUnmatchException;

/**
 * 一库多表策略
 * <p>
 * Copyright: (C), 2020/2/14 20:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RoutingTbStrategy extends AbstractRoutingStrategy {
	
	private static final String DATA_SOURCE_SUFFIX = "dataSource00";
	
	@Override
	public String dataSourceKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException {
		return null;
	}
	
	@Override
	public String tableKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException {
		//前置检查
		Integer routingFiledHashCode =  hashCode(routingField);
		Integer index = routingFiledHashCode % dataSourceRoutingProperties.getTableNum();
		String tableSuffix = tableSuffix(index);
		DataSourceHolder.setTableSuffix(tableSuffix);
		
		return tableSuffix;
	}
}
