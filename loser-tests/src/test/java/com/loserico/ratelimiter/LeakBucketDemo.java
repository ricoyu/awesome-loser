package com.loserico.ratelimiter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法
 * <p>
 * Copyright: (C), 2021-02-20 17:49
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LeakBucketDemo {
	
	/**
	 * 时间刻度
	 */
	private static long time = System.currentTimeMillis();
	
	/**
	 * 桶里面现在的水
	 */
	private static int water = 0;
	
	/**
	 * 桶的大小
	 */
	private static int size = 10;
	
	/**
	 * 出水速度
	 */
	private static int rate = 3;
	
	public static boolean grant() {
		//计算出水的数量
		long now = System.currentTimeMillis();
		int out = (int) ((now - time) / 700 * rate);
		//出水后的剩余
		water = Math.max(0, water - out);
		
		time = now;
		if ((water + 1) < size) {
			water++;
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(8,
				20,
				1,
				TimeUnit.MINUTES,
				new LinkedBlockingQueue<>(1000));
		
		for (int i = 0; i < 500; i++) {
			executor.execute(() -> {
				if (grant()) {
					System.out.println("执行业务逻辑");
				} else {
					System.out.println("限流");
				}
			});
		}
	}
}
