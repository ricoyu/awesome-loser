package com.loserico.cache.factory;

import com.loserico.cache.operations.JedisClusterOperations;
import com.loserico.cache.operations.JedisOperations;
import com.loserico.cache.operations.JedisPoolOperations;
import com.loserico.common.lang.resource.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <p>
 * Copyright: (C), 2019/10/25 14:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class JedisOperationFactory {
	
	private static final String SENTINELS = "redis.sentinels";
	private static final String CLUSTERS = "redis.clusters";
	
	public static JedisOperations create() {
		
		/**
		 * 默认读取classpath下redis.properties文件
		 */
		PropertyReader propertyReader = new PropertyReader("redis");
		/*
		 * Redis的部署类型：single, sentinel, cluster
		 * redis.sentinels 属性存在则采用sentinel形式
		 * redis.clusters 属性存在则用Cluster形式
		 * 都不存在则 Redis 单节点
		 */
		String sentinels = propertyReader.getString(SENTINELS);
		
		if (isNotEmpty(sentinels)) {
			Pool<Jedis> pool = new JedisSentinelPoolFactory().createPool(propertyReader);
			boolean warnmUp = propertyReader.getBoolean("redis.warmUp", true);
			warmUp(pool);
			return new JedisPoolOperations(pool);
		}
		
		if (isNotEmpty(propertyReader.getString(CLUSTERS))) {
			JedisCluster jedisCluster = new JedisClusterPoolFactory().createCluster(propertyReader);
			return new JedisClusterOperations(jedisCluster);
		}
		
		JedisPool jedisPool = new JedisPoolFactory().createPool(propertyReader);
		boolean warnmUp = propertyReader.getBoolean("redis.warmUp", true);
		warmUp(jedisPool);
		return new JedisPoolOperations(jedisPool);
	}
	
	
	/**
	 * 预热JedisPool <p> 由于一些原因(例如超时时间设置较小原因), 有的项目在启动成功后会出现超时。 JedisPool定义最大资源数、最小空闲资源数时, 不会真的把Jedis连接放到池子里, 第一次使用时,
	 * 池子没有资源使用, 会new Jedis, 使用后放到池子里, 可能会有一定的时间开销, 所以也可以考虑在JedisPool定义后, 为JedisPool提前进行预热, 例如以最小空闲数量为预热数量.
	 */
	public static void warmUp(Pool<Jedis> pool) {
		// 不卡住, 不影响Spring的启动
		new Thread(() -> {
			int maxIdle = pool.getNumIdle();
			List<Jedis> minIdleJedisList = new ArrayList<Jedis>(maxIdle);
			
			for (int i = 0; i < maxIdle; i++) {
				Jedis jedis = null;
				try {
					jedis = pool.getResource();
					minIdleJedisList.add(jedis);
					jedis.ping();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
			
			for (int i = 0; i < maxIdle; i++) {
				Jedis jedis = null;
				try {
					jedis = minIdleJedisList.get(i);
					jedis.close();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
			
		}, "<<<< JedisPool warmup thread >>>>");
	}
}
