package com.loserico.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-data-redis-config.xml")
public class SpringDataRedisTest {

	@Resource
	private JedisConnectionFactory jedisConnectionFactory;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void testBootup() {
	}

	@Test
	public void testRedisTemplate() {
		redisTemplate.opsForValue().set("names", "rico YU");

		System.out.println(redisTemplate.opsForValue().get("names"));
	}
}
