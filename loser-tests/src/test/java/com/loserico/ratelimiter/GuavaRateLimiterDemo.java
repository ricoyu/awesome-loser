package com.loserico.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2021-02-26 17:17
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GuavaRateLimiterDemo {
	
	public static ConcurrentHashMap<String, RateLimiter> resourceRateLimiter = new ConcurrentHashMap<>();
	
	public static void createResourceRateLimiter(String resource, double qps) {
		if (resourceRateLimiter.contains(resource)) {
			resourceRateLimiter.get(resource).setRate(qps);
		} else {
			RateLimiter rateLimiter = RateLimiter.create(qps);
			resourceRateLimiter.putIfAbsent(resource, rateLimiter);
		}
	}
	
	public static void main(String[] args) {
		createResourceRateLimiter("order", 50d);
		
		for (int i = 0; i < 5000; i++) {
			new Thread(() -> {
				if (resourceRateLimiter.get("order").tryAcquire(10, TimeUnit.MILLISECONDS)) {
					System.out.println("执行业务逻辑");
				} else {
					System.out.println("限流");
				}
			}).start();
		}
	}
}
