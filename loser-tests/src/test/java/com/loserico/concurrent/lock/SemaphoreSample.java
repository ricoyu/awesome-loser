package com.loserico.concurrent.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 可用于流量控制，限制最大的并发访问数
 * <p>
 * Copyright: (C), 2019/11/20 17:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SemaphoreSample {
	
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(2);
		for (int i = 0; i < 5; i++) {
			new Thread(new Task(semaphore), "三少爷" + i).start();
		}
	}
	
	static class Task implements Runnable {
		
		private Semaphore semaphore;
		
		public Task(Semaphore semaphore) {
			this.semaphore = semaphore;
		}
		
		@Override
		public void run() {
			try {
				semaphore.acquire();
				System.out.println(Thread.currentThread().getName() + ":aquire() at time:" + System.currentTimeMillis());
				
				TimeUnit.MILLISECONDS.sleep(3000);
				semaphore.release();
				System.out.println(Thread.currentThread().getName() + ":release() at time:" + System.currentTimeMillis());
			} catch (InterruptedException e) {
				log.error("", e);
			}
		}
	}
}
