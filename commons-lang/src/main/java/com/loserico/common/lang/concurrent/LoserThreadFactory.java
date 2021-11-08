package com.loserico.common.lang.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 给线程池设置一个特定的名字, 排查问题的时候方便定位
 * <p>
 * Copyright: Copyright (c) 2019-05-27 14:56
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserThreadFactory implements ThreadFactory {
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	/**
	 * 线程池名字前缀
	 */
	private final String poolNamePrefix;
	
	public LoserThreadFactory() {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		poolNamePrefix = "loser-pool-T";
	}
	
	public LoserThreadFactory(String namePrefix) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.poolNamePrefix  = namePrefix + "-T";
	}
	
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(group, r, poolNamePrefix + threadNumber.getAndIncrement(), 0);
		if (thread.isDaemon()) {
			thread.setDaemon(false);
		}
		if (thread.getPriority() != Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		return thread;
	}
}
