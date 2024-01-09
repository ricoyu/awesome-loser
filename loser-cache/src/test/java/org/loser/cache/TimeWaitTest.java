package org.loser.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Copyright: (C), 2023-12-31 15:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class TimeWaitTest {
	
	private AtomicLong counter = new AtomicLong();
	
	@Test
	public void test() {
		String redisHost = "192.168.100.16";
		int redisPort = 6379; // 默认端口
		
		// 创建和关闭大量连接
		for (int i = 0; i < 10000; i++) { // 可以调整循环次数来控制连接数量
			try (Jedis jedis = new Jedis(redisHost, redisPort)) {
				jedis.auth("deepdata$");
				// 可以在这里执行一些简单的命令，如 jedis.ping();
				log.info("第 {} 次ping()", counter.getAndIncrement());
				jedis.ping();
			}
		}
	}
}
