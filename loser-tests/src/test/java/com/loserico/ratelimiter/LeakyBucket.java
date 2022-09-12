package com.loserico.ratelimiter;

/**
 * 漏桶限流算法
 * <p>
 * Copyright: (C), 2022-08-24 16:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LeakyBucket {
	
	public long timestamp = System.currentTimeMillis();
	public long capacity; //桶的容量
	public long rate; //水漏出的速度(每秒系统能处理的请求数)
	public long water; // 当前水量(当前累积请求数)
	
	public boolean limit() {
		long now = System.currentTimeMillis();
		water = Math.max(0, water - ((now - timestamp) / 1000) * rate); //先执行漏水, 计算剩余水量
		timestamp = now;
		if ((water + 1) < capacity) {
			//尝试加水,并且水还未满
			water += 1;
			return true;
		} else {
			//水满, 拒绝加水
			return false;
		}
	}
}
