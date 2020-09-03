package com.loserico.cache.factory;

import com.loserico.common.lang.resource.PropertyReader;

/**
 * Jedis连接池工厂类的工厂类
 * <p>
 * Copyright: Copyright (c) 2019-10-17 14:04
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class JedisPoolFactories {
	
	public static final String SENTINELS = "redis.sentinels";
	public static final String CLUSTERS = "redis.clusters";
	
	private static final PropertyReader propertyReader = new PropertyReader("redis");
	
	public static PoolFactory poolFactory() {
		/*
		 * Redis的部署类型：single, sentinel, cluster
		 * redis.sentinels 属性存在则采用sentinel形式
		 * redis.clusters 属性存在则用Cluster形式
		 * 都不存在则 Redis 单节点
		 */
		String sentinels = propertyReader.getString(SENTINELS);
		
		if (isNotEmpty(sentinels)) {
			return new JedisSentinelPoolFactory();
		} else if (isNotEmpty(propertyReader.getString(CLUSTERS))) {
			return new JedisClusterPoolFactory();
		} else {
			return new JedisPoolFactory();
		}
	}
	
	private static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s.trim());
	}
}
