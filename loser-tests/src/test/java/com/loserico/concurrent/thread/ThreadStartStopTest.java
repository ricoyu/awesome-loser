package com.loserico.concurrent.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-09-30 11:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ThreadStartStopTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		Task task = new Task();
		Thread t1 = new Thread(task);
		System.out.println(t1.getState());
		t1.start();
		System.out.println(t1.getState());
		
		TimeUnit.SECONDS.sleep(1);
		System.out.println(t1.getState());
		task.continueRunning = false;
		log.info("exit");
		System.out.println(t1.getState());
		t1.start();
	}
	
	@Slf4j
	private static class Task implements Runnable {
		
		public static volatile boolean continueRunning = true;
		
		@Override
		public void run() {
			while (continueRunning) {
				log.info("running");
			}
		}
	}
}
