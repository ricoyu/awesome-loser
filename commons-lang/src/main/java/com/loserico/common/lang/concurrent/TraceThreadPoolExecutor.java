package com.loserico.common.lang.concurrent;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/1/2 20:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
	
	/**
	 * 默认核心线程数为CPU核数
	 * 最大线程数为200
	 * keepAliveTime为60秒
	 * 工作队列容量为500
	 */
	public TraceThreadPoolExecutor() {
		super(Runtime.getRuntime().availableProcessors(),
				200,
				60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(500));
	}
	
	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}
	
	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}
	
	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
	                               RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}
	
	@Override
	public void execute(Runnable command) {
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		super.execute(() -> {
			if (contextMap != null) {
				// 如果提交者有本地变量，任务执行之前放入当前任务所在的线程的本地变量中
				MDC.setContextMap(contextMap);
			}
			try {
				command.run();
			} finally {
				// 任务执行完，清除本地变量，以防对后续任务有影响
				MDC.clear();
			}
		});
	}
}
