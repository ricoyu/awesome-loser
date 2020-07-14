package com.loserico.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class KeyEventTest {
	
	public static void main(String[] args) {
		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "118.178.252.68", 6379, 0, "deepdata$", 1);
//		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.103", 6379, 0, "deepdata$", 6);
        Jedis jedis = jedisPool.getResource();
        jedis.psubscribe(new KeyExpiredListener(), "__keyevent@0__:expired");
//        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

    }
}
