package com.loserico.search.support;

import com.loserico.search.annotation.Index;
import com.loserico.search.builder.admin.ElasticSettingsBuilder;
import com.loserico.search.exception.InvalidSettingsException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-05-06 14:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SettingsSupport {
	
	/**
	 * 从@Index 注解中抽取Settings相关信息
	 * @param entity 必须是标注了@Index注解的类
	 * @return ElasticSettingsBuilder
	 */
	public static ElasticSettingsBuilder extractIndexSettings(Class entity) {
		if (entity == null) {
			return null;
		}
		
		Index index = (Index)entity.getAnnotation(Index.class);
		if (index == null) {
			return null;
		}
		
		//主分片数
		int numberOfShards = index.numberOfShards();
		//副本分片数
		int numberOfReplicas = index.numberOfReplicas();
		String defaultPipeline = index.defaultPipeline();
		//拿到的是key=value形式
		String indexRouting = index.indexRouting();
		boolean blocksWrite = index.blocksWrite();
		
		ElasticSettingsBuilder builder = new ElasticSettingsBuilder();
		
		builder.numberOfShards(numberOfShards)
				.numberOfReplicas(numberOfReplicas)
				.blocksWrite(blocksWrite)
				.defaultPipeline(defaultPipeline);
		
		if (isNotBlank(indexRouting)) {
			String[] routing = indexRouting.split("=");
			if (routing.length != 2) {
				throw new InvalidSettingsException("indexRouting can only be name=value pair!");
			}
			
			String routingKey = routing[0];
			String routingValue = routing[1];
			if (isBlank(routingKey) || isBlank(routingValue)) {
				throw new InvalidSettingsException("indexRouting name=value pair cannot be empty!");
			}
			builder.indexRoutingAllocation(routingKey, routingValue);
		}
		
		return builder;
	}
}
