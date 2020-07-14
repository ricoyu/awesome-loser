package com.loserico.sharding.dynamic;

import lombok.extern.slf4j.Slf4j;

/**
 * 多数据源key 缓存类
 * <p>
 * Copyright: (C), 2020/2/14 19:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DataSourceHolder {
	
	private static final ThreadLocal<String> dataSourceKeyHolder = new ThreadLocal<>();
	
	private static final ThreadLocal<String> tableKeyHolder = new ThreadLocal<>();
	
	
	/**
	 * 保存数据源的key
	 * @param dataSourceKey
	 */
	public static void setDataSourceKey(String dataSourceKey) {
		dataSourceKeyHolder.set(dataSourceKey);
	}
	
	/**
	 * 从threadLocal中取出key
	 * @return
	 */
	public static String getDataSourceKey() {
		return dataSourceKeyHolder.get();
	}
	
	/**
	 * 清除key
	 */
	public static void clearDataSourceKey() {
		dataSourceKeyHolder.remove();
	}
	
	public static String getTableKey(){
		return tableKeyHolder.get();
	}
	
	public static void setTableSuffix(String tableKey){
		tableKeyHolder.set(tableKey);
	}
	
	public static void clearTableKey(){
		tableKeyHolder.remove();
	}
}
