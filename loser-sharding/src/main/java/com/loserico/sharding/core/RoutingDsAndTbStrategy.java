package com.loserico.sharding.core;

import com.loserico.sharding.dynamic.DataSourceHolder;
import com.loserico.sharding.exception.RoutingFieldException;
import com.loserico.sharding.exception.RoutingStategyUnmatchException;
import lombok.extern.slf4j.Slf4j;

/**
 * 多表多库路由策略
 * <p>
 * Copyright: (C), 2020/2/14 20:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class RoutingDsAndTbStrategy extends AbstractRoutingStrategy {
	
	@Override
	public String dataSourceKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException {
		Integer hashCode = hashCode(routingField);
		
		//定位库的索引值
		Integer dsIndex = hashCode % dataSourceRoutingProperties.getDataSourceNum();
		//根据库的索引值定位数据源的key
		String dataSourceKey = dataSourceRoutingProperties.getDataSourceKeysMapping().get(dsIndex);
		//放入线程变量
		DataSourceHolder.setDataSourceKey(dataSourceKey);
		
		log.info("根据路由字段:{},值:{},计算出数据库索引值:{},数据源key的值:{}",
				dataSourceRoutingProperties.getRoutingFiled(),
				routingField,
				dsIndex,
				dataSourceKey);
		
		return dataSourceKey;
	}
	
	@Override
	public String tableKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException {
		Integer hashCode = hashCode(routingField);
		Integer tableIndex = hashCode % dataSourceRoutingProperties.getTableNum();
		String tableSuffix = tableSuffix(tableIndex);
		DataSourceHolder.setTableSuffix(tableSuffix);
		log.info("根据路由字段:{},值:{},计算出表索引值:{},表key的值:{}",
				dataSourceRoutingProperties.getRoutingFiled(),
				routingField,
				tableIndex,
				tableSuffix);
		return tableSuffix;
	}
}
