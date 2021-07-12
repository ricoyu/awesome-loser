package com.loserico.common.lang.concurrent;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.loserico.common.lang.concurrent.Policy.ABORT_WITH_REPORT;

/**
 * 线程池工具类
 * <p>
 * Copyright: (C), 2021-07-10 11:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */

public final class Executors {
	
	private static final int NCPUS = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 核心线程数
	 */
	private int corePoolSize;
	
	/**
	 * 是否允许idle的核心线程超时, 超时的线程会被中断
	 */
	private boolean allowCoreThreadTimeOut = false;
	
	/**
	 * 是否提前启动所有核心线程
	 */
	private boolean prestartAllCoreThreads = false;
	
	/**
	 * 线程池最大线程数, 默认200
	 */
	private Integer maximumPoolSize = 200;
	
	/**
	 * 给线程池起个名字
	 */
	private String poolName = "LoserPool";
	
	/**
	 * idle 线程等待新任务的超时时间, 如果等了keepAliveTime还没有新任务到来, 线程中断
	 */
	private long keepAliveTime;
	
	private TimeUnit timeUnit;
	
	/**
	 * 阻塞队列长度, 提交的任务会在这里排队等待被执行
	 */
	private Integer queueSize = 10000;
	
	/**
	 * 线程池饱和策略
	 */
	private Object rejectPolicy = ABORT_WITH_REPORT;
	
	private Executors(String poolName, int corePoolSize, boolean allowCoreThreadTimeOut) {
		this.poolName = poolName;
		this.corePoolSize = corePoolSize;
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}
	
	/**
	 * 自动设置核心线程数为CPU核数+1 <br/>
	 * 不允许核心线程超时
	 *
	 * @return Executors
	 */
	public static Executors newInstance(String poolName) {
		return new Executors(poolName, NCPUS + 1, false);
	}
	
	/**
	 * 显式设置核心线程数以及是否允许核心线程超时
	 *
	 * @param corePoolSize
	 * @param allowCoreThreadTimeOut
	 * @return Executors
	 */
	public static Executors newInstance(String poolName, int corePoolSize, boolean allowCoreThreadTimeOut) {
		return new Executors(poolName, corePoolSize, allowCoreThreadTimeOut);
	}
	
	/**
	 * idle 线程等待新任务的超时时间, 如果等了keepAliveTime还没有新任务到来, 线程中断
	 *
	 * @param keepAliveTime
	 * @param timeUnit
	 * @return Executors
	 */
	public Executors keepAliveTime(Integer keepAliveTime, TimeUnit timeUnit) {
		Objects.requireNonNull(keepAliveTime, "keepAliveTime cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		this.keepAliveTime = keepAliveTime.longValue();
		this.timeUnit = timeUnit;
		return this;
	}
	
	/**
	 * 线程池最大线程数, 默认200
	 *
	 * @param maximumPoolSize
	 * @return Executors
	 */
	public Executors maximumPoolSize(Integer maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
		return this;
	}
	
	/**
	 * 阻塞队列长度, 提交的任务会在这里排队等待被执行
	 *
	 * @param queueSize
	 * @return Executors
	 */
	public Executors queueSize(Integer queueSize) {
		Objects.requireNonNull(queueSize, "queueSize cannot be null!");
		this.queueSize = queueSize;
		return this;
	}
	
	/**
	 * 是否提前启动所有核心线程
	 *
	 * @param prestartAllCoreThreads
	 * @return Executors
	 */
	public Executors prestartAllCoreThreads(boolean prestartAllCoreThreads) {
		this.prestartAllCoreThreads = prestartAllCoreThreads;
		return this;
	}
	
	/**
	 * 设置默认的四种拒绝策略之一
	 *
	 * @param rejectPolicy
	 * @return Executors
	 */
	public Executors rejectPolicy(Policy rejectPolicy) {
		Objects.requireNonNull(rejectPolicy, "rejectPolicy cannot be null!");
		this.rejectPolicy = rejectPolicy;
		return this;
	}
	
	/**
	 * 使用自定义的拒绝策略
	 *
	 * @param rejectPolicy
	 * @return Executors
	 */
	public Executors rejectPolicy(RejectedExecutionHandler rejectPolicy) {
		Objects.requireNonNull(rejectPolicy, "rejectPolicy cannot be null!");
		this.rejectPolicy = rejectPolicy;
		return this;
	}
	
	public ThreadPoolExecutor build() {
		RejectedExecutionHandler handler = null;
		
		if (rejectPolicy instanceof RejectedExecutionHandler) {
			handler = (RejectedExecutionHandler) rejectPolicy;
		} else {
			Policy policy = (Policy) rejectPolicy;
			switch (policy) {
				case CALLER_RUNS:
					handler = new ThreadPoolExecutor.CallerRunsPolicy();
					break;
				case ABORT:
					handler = new ThreadPoolExecutor.AbortPolicy();
					break;
				case ABORT_WITH_REPORT:
					handler = new AbortWithReportPolicy();
					break;
				case DISCARD:
					handler = new ThreadPoolExecutor.DiscardPolicy();
					break;
				case DISCARD_OLDEST:
					handler = new ThreadPoolExecutor.DiscardOldestPolicy();
					break;
			}
		}
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize,
				keepAliveTime, timeUnit,
				new ArrayBlockingQueue<>(queueSize),
				new LoserThreadFactory(poolName),
				handler);
		
		if (prestartAllCoreThreads) {
			executor.prestartAllCoreThreads();
		}
		
		return executor;
	}
	
}
