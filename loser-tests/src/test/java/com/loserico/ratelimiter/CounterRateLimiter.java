package com.loserico.ratelimiter;

/**
 * 计数器法是限流算法里最简单也是最容易实现的一种算法。
 * 比如我们规定, 对于A接口来说, 我们1分钟的访问次数不能超过100个。那么我们可以这么做: 在一开始的时候, 我们可以设置一个计数器counter, 每当一个请求过来的时候, counter就加1,
 * 如果counter的值大于100并且该请求与第一个 请求的间隔时间还在1分钟之内, 那么说明请求数过多;
 * 如果该请求与第一个请求的间隔时间大于1分钟, 且counter的值还在限流范围内, 那么就重置 counter。
 *
 * <p>
 * Copyright: (C), 2022-08-23 16:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CounterRateLimiter {
	
	private long timestamp = System.currentTimeMillis(); //当前时间
	private int reqCount = 0; //初始化计数器
	public final int limit = 100; //时间窗口内最大请求数
	public final long interval = 1000 * 60; //时间窗口ms
	
	public boolean limit() {
		long now = System.currentTimeMillis();
		if (now < timestamp + interval) {
			//在时间窗口内
			reqCount++;
			//判断当前时间窗口内是否超过最大请求控制数
			return reqCount <= limit;
		} else {
			timestamp = now;
			//超时后重置
			reqCount = 1;
			return true;
		}
	}
	
}
