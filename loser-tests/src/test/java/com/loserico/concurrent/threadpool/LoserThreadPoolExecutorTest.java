package com.loserico.concurrent.threadpool;

import com.loserico.concurrent.blockingQueue.LoserArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * Copyright: (C), 2021-07-03 9:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LoserThreadPoolExecutorTest {
	
	public static void main(String[] args) {
		LoserThreadPoolExecutor executor = new LoserThreadPoolExecutor(8, 1000, 10, TimeUnit.SECONDS, new LoserArrayBlockingQueue<>(100));
		executor.execute(() -> {
			while (true) {
				log.info("...执行任务中");
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
			}
		});
		executor.execute(() -> {
			while (true) {
				log.info("...执行任务中");
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
			}
		});
		executor.execute(() -> {
			while (true) {
				log.info("...执行任务中");
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		});
		executor.execute(() -> {
			while (true) {
				log.info("...执行任务中");
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		});
	}
}
