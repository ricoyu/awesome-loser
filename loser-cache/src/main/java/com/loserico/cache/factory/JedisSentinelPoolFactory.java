package com.loserico.cache.factory;

import com.loserico.cache.config.RedisProperties;
import com.loserico.common.lang.resource.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

/**
 * 配置方式
 *   redis.sentinels=192.168.10.101:16379,192.168.10.102:16379,192.168.10.103:16379
 *   redis.masterName=mymaster
 *   redis.password=deepdata$
 *   redis.timeout=2000
 *   redis.db=0
 * Sentinel连接方式的连接池工厂类 
 * <p>
 * Copyright: Copyright (c) 2019-10-17 14:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JedisSentinelPoolFactory implements PoolFactory {

	@Override
	public Pool<Jedis> createPool(PropertyReader propertyReader) {
		//host:port,host:port,host:port
		String sentinels = propertyReader.getString("redis.sentinels");
		String password = propertyReader.getString("redis.password");
		//默认5秒超时
		int timeout = propertyReader.getInt("redis.timeout", 5000); 
		int db = propertyReader.getInt("redis.db", 0);

		String masterName = propertyReader.getString("redis.maserName", "mymaster");
		JedisSentinelPool sentinelPool;
		if (StringUtils.isNotBlank(password)) {
			sentinelPool = new JedisSentinelPool(masterName,
					asList(sentinels.split(",")).stream().collect(toSet()),
					config(propertyReader),
					timeout,
					password,
					db);
		} else {
			sentinelPool = new JedisSentinelPool(masterName,
					asList(sentinels.split(",")).stream().collect(toSet()),
					config(propertyReader),
					timeout,
					null,
					db);
		}

		log.debug("Current master: {}", sentinelPool.getCurrentHostMaster().toString());
		return sentinelPool;
	}

	@Override
	public Pool<Jedis> createPool(RedisProperties redisProperties) {
		return null;
	}

}
