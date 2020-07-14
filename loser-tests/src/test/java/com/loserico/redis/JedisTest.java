package com.loserico.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JedisTest {
	private Jedis jedis;

	@Before
	public void setup() {
		jedis = new Jedis("116.62.136.126", 6379);
//		jedis = new Jedis("192.168.1.4", 6380);
//		jedis = new Jedis("192.168.1.103", 6379);
//		jedis = new Jedis("118.178.252.68", 6379);
		jedis.auth("deepdata$");
		jedis.select(1);
	}

	@Test
	public void testKeys() {
		Set<String> keys = jedis.keys("*");

		for (String key : keys) {
			System.out.println("key = " + key);
		}
	}

	@Test
	public void testExists() {
		boolean isExists = jedis.exists("name");
		assertTrue(isExists);
	}

	@Test
	public void testGetRange() {
		String value = jedis.get("name");
		System.out.println(value);
		String rangedValue = jedis.getrange("name", 0, -1);
		assertEquals(value, rangedValue);
	}

	@Test
	public void testList() {
		jedis.lpush("jedis:list", "rico", "vivi", "zaizai");
		String s1 = jedis.lpop("jedis:list");
		String s2 = jedis.rpop("jedis:list");
		assertEquals("zaizai", s1);
		assertEquals("rico", s2);
		jedis.lpop("jedis:list");
	}

	@Test
	public void testPooled() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.1.3");
		Jedis jedis = pool.getResource();
		jedis.set("MSG", "Hello World");
		assertThat("Hello World", equalTo(jedis.get("MSG")));
		pool.destroy();// close the connection
	}

}