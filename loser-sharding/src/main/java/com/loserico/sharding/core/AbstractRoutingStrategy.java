package com.loserico.sharding.core;

import com.loserico.sharding.config.DataSourceRoutingProperties;
import com.loserico.sharding.constant.RoutingConstant;
import com.loserico.sharding.exception.FormatTableSuffixException;
import com.loserico.sharding.exception.RoutingStategyUnmatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 路由规则抽象类
 *
 * <p>
 * Copyright: (C), 2020/2/14 19:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@EnableConfigurationProperties(DataSourceRoutingProperties.class)
@Slf4j
public abstract class AbstractRoutingStrategy implements RoutingStrategy, InitializingBean {
	
	@Autowired
	protected DataSourceRoutingProperties dataSourceRoutingProperties;
	
	@Override
	public Integer hashCode(String routingField) {
		return Math.abs(routingField.hashCode());
	}
	
	@Override
	public final String tableSuffix(Integer tableSuffix) throws FormatTableSuffixException {
		StringBuilder suffix = new StringBuilder(dataSourceRoutingProperties.getTableSuffixConnect());
		
		try {
			suffix.append(String.format(dataSourceRoutingProperties.getTableSuffixStyle(), tableSuffix));
		} catch (Exception e) {
			log.error("格式化表后缀异常:{}", dataSourceRoutingProperties.getTableSuffixStyle());
			throw new FormatTableSuffixException();
		}
		return suffix.toString();
	}
	
	/**
	 * 工程在启动的时候 检查配置路由参数和 策略是否相匹配
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		switch (dataSourceRoutingProperties.getRoutingStategy()) {
			case RoutingConstant.ROUTING_DS_TABLE_STATEGY:
				checkRoutingDsTableStategyConfig();
				break;
			case RoutingConstant.ROUTGING_DS_STATEGY:
				checkRoutingDsStategyConfig();
				break;
			case RoutingConstant.ROUTGIN_TABLE_STATEGY:
				checkRoutingTableStategyConfig();
				break;
		}
	}
	
	/**
	 * 检查多库 多表配置
	 */
	private void checkRoutingDsTableStategyConfig() {
		if (dataSourceRoutingProperties.getTableNum() <= 1 || dataSourceRoutingProperties.getDataSourceNum() <= 1) {
			log.error("你的配置项routingStategy:{} 是多库多表配置, 数据库个数>1, " +
							"每一个库中表的个数必须>1, 您的配置: 数据库个数:{}, 表的个数:{}", dataSourceRoutingProperties.getRoutingStategy(),
					dataSourceRoutingProperties.getDataSourceNum(), dataSourceRoutingProperties.getTableNum());
			throw new RoutingStategyUnmatchException();
		}
	}
	
	
	/**
	 * 检查多库一表的路由配置项
	 */
	private void checkRoutingDsStategyConfig() {
		if (dataSourceRoutingProperties.getTableNum() != 1 || dataSourceRoutingProperties.getDataSourceNum() <= 1) {
			log.error("你的配置项routingStategy:{} 是多库一表配置, 数据库个数>1," +
							"每一个库中表的个数必须=1, 您的配置: 数据库个数:{}, 表的个数:{}", dataSourceRoutingProperties.getRoutingStategy(),
					dataSourceRoutingProperties.getDataSourceNum(), dataSourceRoutingProperties.getTableNum());
			throw new RoutingStategyUnmatchException();
		}
	}
	
	/**
	 * 检查一库多表的路由配置项
	 */
	private void checkRoutingTableStategyConfig() {
		if (dataSourceRoutingProperties.getTableNum() <= 1 || dataSourceRoutingProperties.getDataSourceNum() != 1) {
			log.error("你的配置项routingStategy:{} 是一库多表配置, 数据库个数=1," +
							"每一个库中表的个数必须>1, 您的配置: 数据库个数:{}, 表的个数:{}", dataSourceRoutingProperties.getRoutingStategy(),
					dataSourceRoutingProperties.getDataSourceNum(), dataSourceRoutingProperties.getTableNum());
			throw new RoutingStategyUnmatchException();
		}
	}
}
