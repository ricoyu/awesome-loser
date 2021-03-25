package com.loserico.ratelimiter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://mp.weixin.qq.com/s/gpBihsfGWXW4WZkTnY2_pw
 * 限流的常用方式: 计数器
 * <p>
 * 计数器是一种比较简单的限流算法，用途比较广泛，在接口层面，很多地方使用这种方式限流。在一段时间内，进行计数，与阀值进行比较，到了时间临界点，将计数器清0。
 * <p>
 * 这里需要注意的是，存在一个时间临界点的问题。
 * <p>
 * 举个例子，在12:01:00到12:01:58这段时间内没有用户请求，然后在12:01:59这一瞬时发出100个请求，OK，
 * <p>
 * 然后在12:02:00这一瞬时又发出了100个请求。这里你应该能感受到，在这个临界点可能会承受恶意用户的大量请求，甚至超出系统预期的承受。
 * Copyright: (C), 2021-02-20 16:45
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CounterDemo {
	
	/**
	 * 时间窗口的起点
	 */
	private static long timestamp = System.currentTimeMillis();
	
	/**
	 * 限制1s内最多100个请求
	 */
	private static int limitCount = 100;
	
	/**
	 * 实际的请求数
	 */
	private static int count = 0;
	
	/**
	 * 时间间隔, 毫秒
	 */
	private static long interval = 1000;
	
	public static boolean grant() {
		long currentMillis = System.currentTimeMillis();
		// 当前时间在窗口范围内
		if (currentMillis < timestamp + interval) {
			//请求数<限制数量则放行
			if (count < limitCount) {
				count++;
				return true;
			}
			return false;
		} else {
			/*
			 * 当前时间已经超出了上一个时间窗口
			 * 重新设置一下时间窗口的起点
			 * 实际请求数清零
			 * 本次请求不放行
			 * (改为实际请求数=1, 本次请求放行?)
			 */
			timestamp = System.currentTimeMillis();
			count = 0;
			return false;
		}
	}
	
	public static void main(String[] args) {
		/*
		 * 核心线程数设为8
		 * 最大线程数设为20
		 * 任务不断提交
		 *   - 还未达到核心线程数 --> 创建新线程
		 *   - 达到核心线程数
		 *       - workerQueue未满 --> 任务添加到workQueue
		 *       - workerQueue已满
		 *         - 还没有达到最大线程数 --> 创建新线程, 运行 workQueue 中等待时间最久的任务, 新任务添加到queue尾部
		 *         - 已达到最大线程数    --> 执行拒绝策略
		 *
		 * 下面demo除了演示限流之外, 还演示了 workerQueue已满, 并且已达到最大线程数, 执行拒绝策略的场景
		 * 可以看到默认的拒绝策略是Rejected Policy
		 * 因为抛出的是: java.util.concurrent.RejectedExecutionException
		 */
		ThreadPoolExecutor executor = new ThreadPoolExecutor(8,
				20,
				1,
				TimeUnit.MINUTES,
				new LinkedBlockingQueue<>(100));
		
		for (int i = 0; i < 500; i++) {
			executor.execute(() -> {
				if (grant()) {
					System.out.println("执行业务逻辑");
				} else {
					System.out.println("被限流");
				}
			});
		}
	}
}
