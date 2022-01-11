package org.loser.cache.ratelimit;

import com.loserico.cache.JedisUtils;
import com.loserico.cache.JedisUtils.ZSET;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * Copyright: (C), 2021-12-29 21:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SlideWindowTest {
	
	/**
	 * 我们可以将请求打造成一个zset数组，当每一次请求进来的时候，value保持唯一，
	 * 可以用UUID生成，而score可以用当前时间戳表示，
	 * 因为score我们可以用来计算当前时间戳之内有多少的请求数量。
	 * 而zset数据结构也提供了range方法让我们可以很轻易的获取到2个时间戳内有多少请求
	 * 
	 * 缺陷:
	 * 1. zset没有清理功能, key会越来越大
	 * 
	 * 解决:
	 * 2. 每次把超出score窗口期的element给移除掉 score < (currentTimestamp - intervalTime)
	 * 
	 * @param intervalTime 限流时间范围, 毫秒
	 * @param limit 限流时间范围内允许多少个请求
	 */
	public static void testSlideWindow(long intervalTime, int limit) {
		Long currentTime = new Date().getTime();
		if (JedisUtils.exists("limit")) {
			Integer count = ZSET.zrangeByScore("limit", currentTime - intervalTime, currentTime).size();
			if (count != null && count > limit) {
				log.error("每{}毫秒最多只能访问{}次", intervalTime, limit);
			}
		}
		ZSET.zadd("limit", currentTime, UUID.randomUUID().toString());
		log.info("访问成功");
	}
	
	@SneakyThrows
	public static void main(String[] args) {
		int intervalTime = 6000;
		int limit = 10;
		for (int i = 0; i < 120; i++) {
			Thread.sleep(10);
			testSlideWindow(intervalTime, limit);
		}
		Thread.sleep(1000);
		log.info("=============================");
		for (int i = 0; i < 120; i++) {
			testSlideWindow(intervalTime, limit);
			Thread.sleep(10);
		}
	}
}
