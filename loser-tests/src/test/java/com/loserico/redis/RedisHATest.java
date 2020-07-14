package com.loserico.redis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class RedisHATest {
	private Jedis jedis;

	@Test
	public void testKeys() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add(new HostAndPort("192.168.1.4", 26379).toString());
		sentinels.add(new HostAndPort("192.168.1.6", 26379).toString());
		sentinels.add(new HostAndPort("192.168.1.103", 26379).toString());
//		sentinels.add(new HostAndPort("192.168.1.4", 5000).toString());
//		sentinels.add(new HostAndPort("192.168.1.4", 5001).toString());
//		sentinels.add(new HostAndPort("192.168.1.4", 5002).toString());
		
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		System.out.println("Current master: " + sentinelPool.getCurrentHostMaster().toString());
		
		jedis = sentinelPool.getResource();
		jedis.set("k1", "v1111");
		System.out.println(jedis.get("k1"));
	}
}
