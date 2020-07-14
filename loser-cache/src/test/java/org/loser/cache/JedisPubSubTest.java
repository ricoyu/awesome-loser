package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;
import redis.clients.jedis.JedisPubSub;

/**
 * <p>
 * Copyright: (C), 2020/4/13 13:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisPubSubTest {
	
	@Test
	public void testPubSub() {
		JedisPubSub subscribe = JedisUtils.subscribe("test-channel", (channel, message) -> {
			System.out.println("收到消息 Channel: " + channel + ", Message: " + message);
		});
		
		long count = JedisUtils.publish("test-channel", "你好");
		System.out.println("收到消息的订阅者数量: " + count);
		
		JedisUtils.unsubscribe(subscribe, "test-channel");
		System.out.println("取消订阅成功");
	}
}
