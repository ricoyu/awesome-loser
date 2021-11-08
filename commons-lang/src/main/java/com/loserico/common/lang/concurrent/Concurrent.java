package com.loserico.common.lang.concurrent;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.loserico.common.lang.exception.ConcurrentOperationException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于JDK1.8 的并发工具类
 * 
 * 合理的最佳线程数估算公式:
 * 最佳线程数目 = ((线程等待时间 + 线程CPU时间) / 线程CPU时间 ) * CPU数目
 * 即
 * 最佳线程数目 = (线程等待时间 / 线程CPU时间 + 1) * CPU数目
 * <p>
 * Copyright: Copyright (c) 2019-05-27 13:27
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public final class Concurrent {

	/** The number of CPUs */
	private static final int NCPUS = Runtime.getRuntime().availableProcessors();

	private static final ThreadFactory defaultThreadFactory = new LoserThreadFactory();

	/**
	 * IO 密集型线程池, 比如执行多个数据库查询操作 <p/>
	 * 用TtlExecutors包装ExecutorService是为了在线程池中执行时也能获取到父线程中设置的ThreadLocal变量
	 * @on
	 */
	private static final ExecutorService IO_POOL = TtlExecutors.getTtlExecutorService(ioConcentratedFixedThreadPool());
	
	/**
	 * 负责执行延迟任务
	 */
	private static ScheduledThreadPoolExecutor DELAY_POOL = new ScheduledThreadPoolExecutor(NCPUS + 1);

	/**
	 * 这个ThreadLocal放的是CompletableFuture对象, 这是在主线程里调用的,
	 * 不需要用阿里的TransmittableThreadLocal
	 */
	private static final ThreadLocal<Set<CompletableFuture<?>>> COMPLETABLE_FUTURE_THREAD_LOCAL = new ThreadLocal<>();

	/**
	 * CPU 内核数 + 1个线程, 适合CPU密集型应用<p/>
	 * 任务队列大小为2600, 线程池已经预热
	 * 
	 * @return ExecutorService
	 * @on
	 */
	public static ExecutorService ncoreFixedThreadPool() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(NCPUS + 1, NCPUS + 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(2600),
				defaultThreadFactory);
		threadPoolExecutor.prestartAllCoreThreads();
		return threadPoolExecutor;
	}

	/**
	 * 2 * CPU 内核数 + 1个线程, 适合IO密集型应用
	 * <p/>
	 * 任务队列大小为2600, 线程池已经预热
	 * 
	 * @return ExecutorService
	 */
	public static ExecutorService ioConcentratedFixedThreadPool() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2 * NCPUS + 1, 2 * NCPUS + 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(2600),
				defaultThreadFactory);
		threadPoolExecutor.prestartAllCoreThreads();
		return threadPoolExecutor;
	}

	/**
	 * 提交一个异步任务并执行
	 * 
	 * @param <T>
	 * @param supplier
	 * @return FutureResult<T>
	 */
	public static <T> FutureResult<T> submit(Supplier<T> supplier) {
		CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier, IO_POOL);
		addCompleteFuture(completableFuture);
		return new FutureResult<>(completableFuture);
	}

	/**
	 * 提交一个异步任务并执行
	 * 
	 * @param task
	 * @return FutureResult<T>
	 */
	public static void execute(Runnable task) {
		CompletableFuture completableFuture = CompletableFuture.runAsync(task, IO_POOL);
		addCompleteFuture(completableFuture);
	}

	/**
	 * 等待所有submit的任务执行完毕, 如果有任务抛异常了, await()方法会抛出一个CompletionException
	 * <p/>
	 * 不管任务是否都成功执行, COMPLETABLE_FUTURE_THREAD_LOCAL都会被清理
	 * 
	 * @throws CompletionException
	 */
	@SuppressWarnings("rawtypes")
	public static void await() {
		Set<CompletableFuture<?>> set = COMPLETABLE_FUTURE_THREAD_LOCAL.get();
		if (set == null || set.isEmpty()) {
			throw new ConcurrentOperationException("没有提交过任何任务就别瞎等了, 哥~!");
		}
		try {
			int taskCount = set.size();
			log.debug("等待所有{}个任务执行完毕", taskCount);
			CompletableFuture[] completableFutures = set.toArray(new CompletableFuture[taskCount]);
			CompletableFuture.allOf(completableFutures).join();
		} finally {
			COMPLETABLE_FUTURE_THREAD_LOCAL.remove();
		}
	}

	private static void addCompleteFuture(CompletableFuture<?> completableFuture) {
		Set<CompletableFuture<?>> set = COMPLETABLE_FUTURE_THREAD_LOCAL.get();
		if (set == null) {
			set = new HashSet<>();
			COMPLETABLE_FUTURE_THREAD_LOCAL.set(set);
		}
		log.debug("提交第{}个任务", set.size() + 1);
		set.add(completableFuture);
	}

	public static void awaitTermination(ExecutorService executorService, long timeout, TimeUnit timeUnit) {
		Objects.requireNonNull(executorService);
		executorService.shutdown();
		try {
			executorService.awaitTermination(timeout, timeUnit);
		} catch (InterruptedException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 延迟指定时间执行一次(注意: 是执行一次)
	 * @param task
	 * @param delay
	 * @param timeUnit
	 */
	public static void schedule(Runnable task, long delay, TimeUnit timeUnit) {
		DELAY_POOL.schedule(task, delay, timeUnit);
	}
	
	static class ThreadLocalSupplier<V, T> implements Supplier<V> {

		private final ThreadLocal<T> threadLocal;

		private final T value;

		private final Supplier<V> supplier;

		public ThreadLocalSupplier(ThreadLocal<T> threadLocal, T value, Supplier<V> supplier) {
			this.threadLocal = threadLocal;
			this.supplier = supplier;
			this.value = value;
		}

		@Override
		public V get() {
			T oldValue = threadLocal.get();
			try {
				threadLocal.set(value);
				return supplier.get();
			} finally {
				threadLocal.set(oldValue);
			}
		}

	}
}
