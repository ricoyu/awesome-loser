package com.loserico.cache.factory;

import com.loserico.cache.config.RedisProperties;
import com.loserico.common.lang.resource.PropertyReader;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

/**
 * <p>
 * Copyright: (C), 2019/10/23 18:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisClusterPoolFactory implements PoolFactory {
	
	@Override
	public JedisCluster createCluster(PropertyReader propertyReader) {
		String clusters = propertyReader.getString(JedisPoolFactories.CLUSTERS);
		Set<HostAndPort> nodes = asList(clusters.split(","))
				.stream()
				.map((hostPort) -> {
					String[] hostAndPort = hostPort.split(":");
					return new HostAndPort(hostAndPort[0].trim(), Integer.parseInt(hostAndPort[1].trim()));
				}).collect(toSet());
		
		JedisPoolConfig poolConfig = config(propertyReader);
		String password = propertyReader.getString("redis.password");
		
		// 默认50秒连接超时
		int connectionTimeout = propertyReader.getInt("redis.connectionTimeout", 50000);
		//执行redis命令默认1秒超时
		int socketTimeout = propertyReader.getInt("redis.socketTimeout", 1000);
		//出现异常最大重试次数
		int maxAttempts = propertyReader.getInt("redis.cluster.maxAttempts", 3);
		return new JedisCluster(nodes, connectionTimeout, socketTimeout, maxAttempts, password, poolConfig);
	}
	
	@Override
	public Pool<Jedis> createPool(RedisProperties redisProperties) {
		return null;
	}
}
