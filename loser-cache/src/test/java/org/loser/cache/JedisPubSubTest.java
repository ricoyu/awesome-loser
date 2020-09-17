package org.loser.cache;

import com.loserico.cache.JedisUtils;
import lombok.SneakyThrows;
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
		JedisPubSub subscribe = JedisUtils.subscribe((channel, message) -> {
			System.out.println("收到消息 Channel: " + channel + ", Message: " + message);
		}, "test-channel");
		
		long count = JedisUtils.publish("test-channel", "你好");
		System.out.println("收到消息的订阅者数量: " + count);
		
		JedisUtils.unsubscribe(subscribe, "test-channel");
		System.out.println("取消订阅成功");
	}
	
	@SneakyThrows
	public static void main(String[] args) {
		JedisUtils.psubscribe((channel, message) -> {
			System.out.println(channel);
			System.out.println(message);
		}, "__keyevent@*__:expired");
		
		/*JedisUtils.psubscribe((channel, message) -> {
			System.out.println(channel);
			System.out.println(message);
		}, "channel1");
		
		
		JedisUtils.publish("channel1", "hi");*/
		
		//Thread.currentThread().join();
		/*Thread t1 = new Thread(() -> {
			Lock lock = JedisUtils.blockingLock("rico");
			lock.lock();
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (lock.locked()) {
					lock.unlock();
				}
			}
		}, "t1");
		Thread t2 = new Thread(() -> {
			Lock lock = JedisUtils.blockingLock("rico");
			lock.lock();
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (lock.locked()) {
					lock.unlock();
				}
			}
		}, "t2");
		t1.start();
		t2.start();*/
	}
}
