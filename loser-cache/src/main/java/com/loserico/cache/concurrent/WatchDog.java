package com.loserico.cache.concurrent;

/**
 * 负责不断刷新分布式锁的过期时间
 * <p>
 * Copyright: (C), 2020/3/28 18:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class WatchDog {
	
	/**
	 * key过期时间
	 */
	private long ttlMillis;
	
	public WatchDog(long ttlMillis) {
		this.ttlMillis = ttlMillis;
	}
		
	
}
