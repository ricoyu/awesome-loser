package com.loserico.cache.pool;

import com.loserico.cache.config.RedisProperties;
import com.loserico.cache.factory.JedisPoolFactories;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.util.Pool;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 可动态切换的Jedis Pool
 * <p>
 * Copyright: Copyright (c) 2019-10-17 14:12
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Slf4j
public abstract class AbstractRoutingRedisPool<T> extends Pool<T> {

	/**
	 * 如果要切换的目标JedisPool不存在, 是否使用默认的一个JedisPool替代
	 */
	private boolean lenientFallback = true;

	/**
	 * 以JedisPool名字为key, Pool实例为value存储, 方便根据名字取出对应的实例
	 */
	private Map<Object, Pool<T>> targetPools = new HashMap<>();

	/**
	 * 以JedisPool名字为key, 对应Pool的配置属性为value
	 */
	private Map<String, RedisProperties> redisPropertiesMap = new HashMap<>();

	/**
	 * 这是默认的一个JedisPool
	 */
	private Pool<T> defaultTargetPool;

	/**
	 * 默认JedisPool的名字
	 */
	private String defaultTargetPoolName;

	/**
	 * Specify whether to apply a lenient fallback to the default Pool
	 * if no specific Pool could be found for the current lookup key.
	 * <p>Default is "true", accepting lookup keys without a corresponding entry
	 * in the target Pool map - simply falling back to the default Pool
	 * in that case.
	 * <p>Switch this flag to "false" if you would prefer the fallback to only apply
	 * if the lookup key was {@code null}. Lookup keys without a Pool
	 * entry will then lead to an IllegalStateException.
	 *
	 * @see #setTargetPools
	 * @see #setDefaultTargetPool
	 * @see #determineCurrentLookupKey()
	 */
	public void setLenientFallback(boolean lenientFallback) {
		this.lenientFallback = lenientFallback;
	}

	/**
	 * Determine the current lookup key. This will typically be implemented to check a thread-bound
	 * transaction context.
	 * <p>
	 * Allows for arbitrary keys. The returned key needs to match the stored lookup key type, as
	 * resolved by the method.
	 *
	 * @return JedisPool的名字
	 */
	protected abstract Object determineCurrentLookupKey();

	/**
	 * Retrieve the current target Pool. Determines the
	 * {@link #determineCurrentLookupKey() current lookup key}, performs
	 * a lookup in the {@link #setTargetPools targetPools} map,
	 * falls back to the specified
	 * {@link #setDefaultTargetPool default target Pool} if necessary.
	 *
	 * @see #determineCurrentLookupKey()
	 */
	public Pool<T> determineTargetPool() {
		Object lookupKey = determineCurrentLookupKey();
		log.info("Current redis pool {}", lookupKey);

		Pool<T> pool = this.targetPools.get(lookupKey);

		/**
		 * 找不到指定的JedisPool时, 决定是否取默认的一个JedisPool作为备胎
		 */
		if (pool == null && (this.lenientFallback || lookupKey == null)) {
			String defaultPoolName = defaultTargetPoolName.toLowerCase();
			log.info("Switch to default redis pool {}", defaultPoolName);
			pool = this.targetPools.get(defaultPoolName);
		}
		
		if (pool == null) {
			throw new IllegalStateException("Cannot determine target Pool for lookup key [" + lookupKey + "]");
		}
		return pool;
	}


	@PostConstruct
	public void init() {
		if (redisPropertiesMap != null && !redisPropertiesMap.isEmpty()) {
			for (Entry<String, RedisProperties> entry : redisPropertiesMap.entrySet()) {
				Pool<T> pool = (Pool<T>) JedisPoolFactories.poolFactory().createPool(entry.getValue());
				targetPools.put(entry.getKey().toLowerCase(), pool);
			}
		}
	}
}
