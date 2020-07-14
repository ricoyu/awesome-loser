package com.loserico.redis;

import com.loserico.common.lang.utils.StringUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class JedisKeysTest {

	@Test
	public void testGetAuthKeys() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add(new HostAndPort("192.168.1.4", 26379).toString());
		sentinels.add(new HostAndPort("192.168.1.6", 26379).toString());
		sentinels.add(new HostAndPort("192.168.1.6", 26380).toString());

		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels, "deepdata$");
		System.out.println("Current master: " + sentinelPool.getCurrentHostMaster().toString());

		Jedis jedis = sentinelPool.getResource();
		jedis.select(1);

		Set<String> shadowKeys = jedis.keys("*shadow:*");
		Set<String> tokens = shadowKeys.stream().map((shadowToken) -> {
			String[] shadowTokenArr = shadowToken.split(":");
			return StringUtils.subStr(shadowTokenArr[1], 0, -1);
		}).collect(toSet());
		
		//所有的token
		tokens.forEach(System.out::println);
		
		//所有的device
		Set<String> platformKeys = tokens.stream().map(token -> "platform:" + token).collect(toSet());
		List<String> platforms = jedis.mget(platformKeys.stream().toArray(String[]::new));
		platforms.forEach(System.out::println);
		Map<String, Long> result = platforms.stream().collect(groupingBy(Function.identity(), counting()));
		System.out.println(JacksonUtils.toJson(result));
	}
}
