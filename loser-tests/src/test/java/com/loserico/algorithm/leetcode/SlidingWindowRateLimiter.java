package com.loserico.algorithm.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 滑动时间窗口限流算法
 * <p/>
 * Copyright: Copyright (c) 2024-11-08 9:58
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SlidingWindowRateLimiter {

	private int maxRequests; // 窗口内允许的最大请求数
	private long windowSizeMillis; // 时间窗口大小（毫秒）

	private Deque<Long> slidingWindows; // 用于存储请求时间戳的队列

	public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis) {
		this.maxRequests = maxRequests;
		this.windowSizeMillis = windowSizeMillis;
		this.slidingWindows = new LinkedList<>();
	}

	/**
	 * 判断请求是否被允许
	 * synchronized关键字确保在并发环境下对请求队列的操作是线程安全的。
	 *
	 * @return
	 */
	public synchronized boolean allowRequest() {
		long currentTime = System.currentTimeMillis();
		long windowStartTime = currentTime - windowSizeMillis;

		// 移除窗口外的请求记录
		while (!slidingWindows.isEmpty() && slidingWindows.peek() < windowStartTime) {
			slidingWindows.poll();
		}

		// 检查当前窗口内的请求数量是否超出限制
		if (slidingWindows.size() < maxRequests) {
			slidingWindows.addLast(currentTime);// 记录当前请求时间
			return true;// 允许请求
		} else {
			return false;// 超出限制，拒绝请求
		}
	}

	public static void main(String[] args) {
		// 每10秒内最多允许5个请求
		SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(5, 10000);

		// 模拟请求过程
		for (int i = 0; i < 50; i++) {
			if (rateLimiter.allowRequest()) {
				System.out.println("请求被允许");
			} else {
				System.out.println("请求被拒绝");
			}
			// 模拟请求间隔
			try {
				Thread.sleep(1000); // 每2秒发起一次请求
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
