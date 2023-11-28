package com.loserico.algorithm.ratelimit;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 优化后的滑动时间窗口限流器，适用于高并发场景。
 * <p>
 * Copyright: Copyright (c) 2023-10-27 10:15
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class OptimizedSlidingTimeWindowRateLimiter {
	
	// 整个时间窗口的大小（以毫秒为单位）
	private final long windowSizeInMillis;
	// 子窗口的总数
	private final int totalSubWindows;
	// 每个子窗口允许的最大请求数
	private final int[] maxRequestsPerSubWindow;
	// 使用原子整数数组来跟踪每个子窗口的请求计数
	private final AtomicInteger[] requestCounts;
	// 最后一次请求的时间
	private long lastTime = System.currentTimeMillis();
	// 当前活跃的子窗口索引
	private int currentSubWindowIndex = 0;
	// 用于确保线程安全性的锁
	private final ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 构造函数
	 *
	 * @param windowSizeInMillis      整个时间窗口的大小（以毫秒为单位）
	 * @param maxRequestsPerSubWindow 每个子窗口允许的最大请求数, 数组的长度表示子窗口的总数
	 */
	public OptimizedSlidingTimeWindowRateLimiter(long windowSizeInMillis, int[] maxRequestsPerSubWindow) {
		this.windowSizeInMillis = windowSizeInMillis;
		this.totalSubWindows = maxRequestsPerSubWindow.length;
		this.maxRequestsPerSubWindow = maxRequestsPerSubWindow;
		this.requestCounts = new AtomicInteger[totalSubWindows];
		for (int i = 0; i < totalSubWindows; i++) {
			requestCounts[i] = new AtomicInteger();
		}
	}
	
	/**
	 * 尝试获取请求权限。
	 *
	 * @return 如果在当前子窗口内允许请求，则返回true，否则返回false。
	 */
	public boolean tryAcquire() {
		lock.lock();
		try {
			long now = System.currentTimeMillis();
			long elapsedMillis = now - lastTime;
			
			int targetSubWindow = (int) ((elapsedMillis * totalSubWindows) / windowSizeInMillis);
			
			if (targetSubWindow != currentSubWindowIndex) {
				for (int i = 1; i <= (targetSubWindow - currentSubWindowIndex + totalSubWindows) % totalSubWindows; i++) {
					requestCounts[(currentSubWindowIndex + i) % totalSubWindows].set(0);
				}
				currentSubWindowIndex = targetSubWindow;
				lastTime = now;
			}
			
			return requestCounts[currentSubWindowIndex].incrementAndGet() <= maxRequestsPerSubWindow[currentSubWindowIndex];
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		OptimizedSlidingTimeWindowRateLimiter limiter = new OptimizedSlidingTimeWindowRateLimiter(1000, new int[]{3, 2});
		
		for (int i = 0; i < 10; i++) {
			System.out.println("Request " + (i + 1) + ": " + (limiter.tryAcquire() ? "Allowed" : "Denied"));
			Thread.sleep(100);
		}
	}
}
