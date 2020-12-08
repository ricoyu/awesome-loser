package com.loserico.common.lang.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池拒绝策略:<p>
 * 1.记录线程池的核心线程数, 活跃数, 已完成数等信息, 以及调用线程的堆栈信息, 便于排查<p>
 * 2.抛出异常中断执行
 * <p>
 * Copyright: Copyright (c) 2020-12-07 13:57
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RejectedPolicyWithReport implements RejectedExecutionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(RejectedPolicyWithReport.class);
	
	private static volatile long lastPrintTime = 0;
	
	private static final long TEN_MINUTES_MILLS = 10 * 60 * 1000;
	
	private static Semaphore guard = new Semaphore(1);
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			String title = "Thread pool execute reject policy!!";
			String msg = MessageFormat.format(
					"{0}{1}" +
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
					executor.getCorePoolSize(),
					executor.getPoolSize(),
					executor.getQueue().size(),
					executor.getActiveCount(),
					executor.getCompletedTaskCount(),
					executor.getTaskCount(),
					executor.getLargestPoolSize(),
					executor.getMaximumPoolSize(),
					executor.getKeepAliveTime(TimeUnit.SECONDS),
					executor.isShutdown(),
					executor.isTerminated(),
					Thread.currentThread().getName(), System.lineSeparator());
			log.info(msg);
			/*
			 * 记录线程堆栈信息包括锁争用信息
			 */
			threadDump();
		} catch (Exception ex) {
			log.error("RejectedPolicyWithReport rejectedExecution error", ex);
		}
		throw new RejectedExecutionException("Loser thread pool reached maximum pool size " + executor.getMaximumPoolSize() + ", so your task is rejected!");
	}
	
	/**
	 * 获取线程dump信息<p>
	 * 注意: 该方法默认会记录所有线程和锁信息. 虽然方便debug, 使用时最好加开关和间隔调用, 否则可能会增加latency<p>
	 * 1.当前线程的基本信息:id,name,state<p>
	 * 2.堆栈信息<p>
	 * 3.锁相关信息(可以设置不记录)<p>
	 * 默认在log记录<p>
	 */
	private void threadDump() {
		long now = System.currentTimeMillis();
		// 每隔10分钟dump一次
		if (now - lastPrintTime < TEN_MINUTES_MILLS) {
			return;
		}
		if (!guard.tryAcquire()) {
			return;
		}
		// 异步dump线程池信息 
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(() -> {
			try {
				ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
				StringBuilder sb = new StringBuilder();
				for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
					sb.append(getThreadDumpString(threadInfo));
				}
				log.error("thread dump info:", sb.toString());
			} catch (Exception e) {
				log.error("thread dump error", e);
			} finally {
				guard.release();
				pool.shutdown();
			}
			lastPrintTime = System.currentTimeMillis();
		});
	}
	
	@SuppressWarnings("all")
	private String getThreadDumpString(ThreadInfo threadInfo) {
		StringBuilder sb = new StringBuilder("\"" + threadInfo.getThreadName() + "\"" + " Id=" + threadInfo.getThreadId() + " " + threadInfo.getThreadState());
		if (threadInfo.getLockName() != null) {
			sb.append(" on " + threadInfo.getLockName());
		}
		if (threadInfo.getLockOwnerName() != null) {
			sb.append(" owned by \"" + threadInfo.getLockOwnerName() +
					"\" Id=" + threadInfo.getLockOwnerId());
		}
		if (threadInfo.isSuspended()) {
			sb.append(" (suspended)");
		}
		if (threadInfo.isInNative()) {
			sb.append(" (in native)");
		}
		sb.append('\n');
		int i = 0;
		
		StackTraceElement[] stackTrace = threadInfo.getStackTrace();
		MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
		for (; i < stackTrace.length && i < 32; i++) {
			StackTraceElement ste = stackTrace[i];
			sb.append("\tat " + ste.toString());
			sb.append('\n');
			if (i == 0 && threadInfo.getLockInfo() != null) {
				Thread.State ts = threadInfo.getThreadState();
				switch (ts) {
					case BLOCKED:
						sb.append("\t-  blocked on " + threadInfo.getLockInfo());
						sb.append('\n');
						break;
					case WAITING:
						sb.append("\t-  waiting on " + threadInfo.getLockInfo());
						sb.append('\n');
						break;
					case TIMED_WAITING:
						sb.append("\t-  waiting on " + threadInfo.getLockInfo());
						sb.append('\n');
						break;
					default:
				}
			}
			
			for (MonitorInfo mi : lockedMonitors) {
				if (mi.getLockedStackDepth() == i) {
					sb.append("\t-  locked " + mi);
					sb.append('\n');
				}
			}
		}
		if (i < stackTrace.length) {
			sb.append("\t...");
			sb.append('\n');
		}
		
		LockInfo[] locks = threadInfo.getLockedSynchronizers();
		if (locks.length > 0) {
			sb.append("\n\tNumber of locked synchronizers = " + locks.length);
			sb.append('\n');
			for (LockInfo li : locks) {
				sb.append("\t- " + li);
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}
}
