package com.loserico.common.lang.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池<p>
 * 1.监控线程池状态及异常关闭等情况<p>
 * 2.监控线程池运行时的各项指标, 比如:任务等待数、已完成任务数、任务异常信息、核心线程数、最大线程数等<p>
 * http://ifeve.com/java%E8%B8%A9%E5%9D%91%E8%AE%B0%E7%B3%BB%E5%88%97%E4%B9%8B%E7%BA%BF%E7%A8%8B%E6%B1%A0/
 *
 * <p>
 * Copyright: Copyright (c) 2020-12-07 13:47
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserThreadExecutor extends ThreadPoolExecutor {
	
	private static final Logger log = LoggerFactory.getLogger(LoserThreadExecutor.class);
	
	private TimeUnit timeUnit;
	
	/**
	 * @param corePoolSize    核心线程数
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime
	 * @param unit
	 */
	public LoserThreadExecutor(int corePoolSize,
	                           int maximumPoolSize,
	                           long keepAliveTime,
	                           TimeUnit unit) {
		super(corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				unit,
				new SynchronousQueue<>(),
				new LoserThreadFactory(),
				new RejectedPolicyWithReport());
		this.timeUnit = unit;
	}
	
	/**
	 * @param corePoolSize    核心线程数
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime
	 * @param unit
	 */
	public LoserThreadExecutor(int corePoolSize,
	                           int maximumPoolSize,
	                           long keepAliveTime,
	                           TimeUnit unit, 
                               String poolNamePrefix) {
		super(corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				unit,
				new SynchronousQueue<>(),
                new LoserThreadFactory(poolNamePrefix),
				new RejectedPolicyWithReport());
		this.timeUnit = unit;
	}
	
	/**
	 * @param corePoolSize    核心线程数
	 * @param maximumPoolSize 最大线程数
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue       等待队列
	 * @param threadFactory
	 * @param handler         决绝策略
	 */
	public LoserThreadExecutor(int corePoolSize,
	                           int maximumPoolSize,
	                           long keepAliveTime,
	                           TimeUnit unit,
	                           BlockingQueue<Runnable> workQueue,
	                           ThreadFactory threadFactory,
	                           RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.timeUnit = unit;
	}
	
	@Override
	public void shutdown() {
		/*
		 * 线程池将要关闭事件
		 * 此方法会等待线程池中的任务(包括正在执行的任务和队列中等待的任务)执行完毕再关闭
		 */
		monitor("ThreadPool will be shutdown:");
		super.shutdown();
	}
	
	@Override
	public List<Runnable> shutdownNow() {
		/*
		 * 此方法会立即关闭线程池, 同时会返回队列中等待的任务
		 */
		monitor("ThreadPool going to immediately be shutdown:");
		/*
		 * 这是在等待队列中还没有执行, 被丢弃的任务
		 */
		List<Runnable> dropTasks = null;
		try {
			dropTasks = super.shutdownNow();
			log.error("ThreadPool discard task count:{}", dropTasks.size());
		} catch (Exception e) {
			log.error("ThreadPool shutdownNow error", e);
		}
		return dropTasks;
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		/*
		 * 监控线程池运行时的各项指标
		 */
		monitor("ThreadPool monitor data:");
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable e) {
		if (e != null) {
			log.error("Unknown exception caught in ThreadPool afterExecute:", e);
		}
	}
	
	/**
	 * 监控线程池运行时的各项指标
	 * 比如:任务等待数、任务异常信息、已完成任务数、核心线程数、最大线程数等
	 *
	 * @param title
	 */
	private void monitor(String title) {
		try {
			// 线程池监控信息记录, 这里需要注意写ES的时机,尤其是多个子线程的日志合并到主流程的记录方式
			String threadPoolMonitor = MessageFormat.format(
					"{0}{1} " +
							"Core pool size:{2}, " +
							"Current pool size:{3}, " +
							"Queue wait size:{4}, " +
							"Active count:{5}, " +
							"Completed task count:{6}, " +
							"Task count:{7}, " +
							"Largest pool size:{8}, " +
							"Max pool size:{9}, " +
							"Keep alive time:{10}, " +
							"Is shutdown:{11}, " +
							"Is terminated:{12}, " +
							"Thread name:{13}{14}",
					System.lineSeparator(), title,
					this.getCorePoolSize(),
					this.getPoolSize(),
					this.getQueue().size(),
					this.getActiveCount(),
					this.getCompletedTaskCount(),
					this.getTaskCount(),
					this.getLargestPoolSize(),
					this.getMaximumPoolSize(),
					this.getKeepAliveTime(timeUnit != null ? timeUnit : TimeUnit.SECONDS),
					this.isShutdown(),
					this.isTerminated(),
					Thread.currentThread().getName(), System.lineSeparator());
			log.info(threadPoolMonitor);
		} catch (Exception e) {
			log.error("ThreadPool monitor error", e);
		}
	}
	
	static class LoserThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
        /**
         * 线程池名字前缀
         */
		private final String poolNamePrefix;
		
		LoserThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			poolNamePrefix = "loser-pool-" + poolNumber.getAndIncrement() + "-thread-";
		}
		
		LoserThreadFactory(String namePrefix) {
		    this.poolNamePrefix = namePrefix;
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = namePrefix + "-" + poolNumber.getAndIncrement() + "-thread-";
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
}
