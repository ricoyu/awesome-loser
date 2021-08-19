package com.loserico.concurrent.interrupt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * <p>
 * Copyright: (C), 2021-07-25 15:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class InterruptedTest2 {
	
	@SneakyThrows
	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			TransferQueue<String> queue = new LinkedTransferQueue<>();
			
			while (!Thread.currentThread().isInterrupted()) {
				try {
					log.info("For 3 seconds the thread {} will try to poll an element from queue ...", Thread.currentThread().getName());
					queue.poll(3000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException ex) {
					log.error("InterruptedException! The thread {} was interrupted!", Thread.currentThread().getName());
					log.info("Current Thread interrupted status {}", Thread.currentThread().isInterrupted());
					Thread.currentThread().interrupt();
					log.info("Current Thread interrupted status {}", Thread.currentThread().isInterrupted());
				}
			}
			log.info("The execution was stopped!");
		});
		
		thread.start();
		Thread.sleep(1500);
		log.info("线程阻塞时候的interrupted状态 {}", thread.isInterrupted());
		thread.interrupt();
	}
}
