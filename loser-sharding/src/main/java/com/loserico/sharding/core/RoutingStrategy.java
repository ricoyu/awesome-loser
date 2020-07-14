package com.loserico.sharding.core;

import com.loserico.sharding.exception.FormatTableSuffixException;
import com.loserico.sharding.exception.RoutingFieldException;
import com.loserico.sharding.exception.RoutingStategyUnmatchException;

/**
 * 路由接口
 * <p>
 * Copyright: (C), 2020/2/14 18:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface RoutingStrategy {
	
	/**
	 * 根据路由规则和路由字段计算出数据源KEY
	 *
	 * @param routingField
	 * @return
	 * @throws RoutingStategyUnmatchException
	 * @throws RoutingFieldException
	 */
	public String dataSourceKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException;
	
	/**
	 * 计算routingField字段的hashCode值
	 *
	 * @param routingField
	 * @return
	 */
	public Integer hashCode(String routingField);
	
	/**
	 * 计算一个库所在表的KEY
	 *
	 * @param routingField
	 * @return
	 * @throws RoutingStategyUnmatchException
	 * @throws RoutingFieldException
	 */
	public String tableKey(String routingField) throws RoutingStategyUnmatchException, RoutingFieldException;
	
	/**
	 * 获取表的后缀
	 * @param tableIndex
	 * @return
	 * @throws FormatTableSuffixException
	 */
	public String tableSuffix(Integer tableIndex) throws FormatTableSuffixException;
}
