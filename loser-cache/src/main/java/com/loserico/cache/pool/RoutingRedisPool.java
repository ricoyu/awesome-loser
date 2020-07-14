package com.loserico.cache.pool;

import com.loserico.cache.context.RedisPoolContextHolder;

/**
 * 可以动态切换Redis Pool的Pool ;-) 
 * <p>
 * Copyright: Copyright (c) 2019-10-17 14:12
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RoutingRedisPool<T> extends AbstractRoutingRedisPool<T> {

	@Override
	protected Object determineCurrentLookupKey() {
		return RedisPoolContextHolder.getRedisPool();
	}

}
