package com.loserico.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ConnectionManager {
	
	private static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.3", 6479, 0);

	public static Jedis get() {
		return jedisPool.getResource();
	}

	public static void set(Jedis jedis) {
		jedis.close();
	}

	public static void close() {
		jedisPool.destroy();
	}
}