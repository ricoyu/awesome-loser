package com.loserico.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2021-01-28 17:06
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadpoolExecutorStatusTest {
	
	private static ExecutorService pool = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100000));
	
	public static void main(String[] args) {
		
	}
}
