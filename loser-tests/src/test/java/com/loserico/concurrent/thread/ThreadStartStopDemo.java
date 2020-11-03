package com.loserico.concurrent.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-09-30 14:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ThreadStartStopDemo {
	
	@SneakyThrows
	public static void main(String[] args) {
		LoserThreadFactory threadFactory = new LoserThreadFactory("loser");
		Task task = new Task();
		Thread thread = threadFactory.newThread(task);
		thread.start();
		
		TimeUnit.SECONDS.sleep(1);
		task.running = false;
		
		TimeUnit.SECONDS.sleep(1);
		task.running = true;
		thread.interrupt();
		
		TimeUnit.SECONDS.sleep(1);
		task.running = false;
	}
	
	private static class Task implements Runnable {
		
		public volatile boolean running = true;
		
		@Override
		public void run() {
			while (running) {
				System.out.println("...");
			}
			
			log.info("go to bed");
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
				log.info("interrupted");
				run();
			}
		}
	}
}
