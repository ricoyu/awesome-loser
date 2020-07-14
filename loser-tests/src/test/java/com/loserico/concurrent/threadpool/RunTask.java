package com.loserico.concurrent.threadpool;

/**
 * <p>
 * Copyright: (C), 2019/12/1 11:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RunTask implements Runnable {
	
	@Override
	public void run() {
		System.out.println("Thread name:" + Thread.currentThread().getName());
	}
}
