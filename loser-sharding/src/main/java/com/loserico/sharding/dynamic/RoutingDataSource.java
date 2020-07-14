package com.loserico.sharding.dynamic;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源类
 * <p>
 * Copyright: (C), 2020/2/14 21:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
	
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceHolder.getDataSourceKey();
	}
}
