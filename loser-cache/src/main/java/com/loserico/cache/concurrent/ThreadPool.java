package com.loserico.cache.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/30 18:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ThreadPool {
	
	private static final int NCPUS = Runtime.getRuntime().availableProcessors();
	
	/**
	 * The number of times to spin before blocking in timed waits.
	 * The value is empirically derived -- it works well across a
	 * variety of processors and OSes. Empirically, the best value
	 * seems not to vary with number of CPUs (beyond 2) so is just
	 * a constant.
	 */
	static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;
	
	/**
	 * The number of times to spin before blocking in untimed waits.
	 * This is greater than timed value because untimed waits spin
	 * faster since they don't need to check times on each spin.
	 */
	static final int maxUntimedSpins = maxTimedSpins * 16;
	
	/**
	 * The number of nanoseconds for which it is faster to spin
	 * rather than to use timed park. A rough estimate suffices.
	 */
	static final long spinForTimeoutThreshold = 1000L;
	
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder()
			.setNameFormat("loser-cache-线程-%d")
			.setDaemon(true)
			.build();
	
	private ThreadPool() {
	}
	
	/**
	 * 创建一个核心线程数为CPU核数*2, 最大线程数为1000, workQueue长度为1000的线程池
	 *
	 * @return ExecutorService
	 */
	public static ExecutorService newThreadPool() {
		return new ThreadPoolExecutor(NCPUS * 2,
				1000,
				60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(1000),
				threadFactory);
	}
}
