package com.loserico.concurrent.blockingQueue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.*;

@Slf4j
public class SynchronousQueueExample2 {

	private static final AtomicInteger SEQ = new AtomicInteger(1);

	public static void main(String[] args) {
		SynchronousQueue<String> queue = new SynchronousQueue<>();
		new Thread("Consumer") {
			public void run() {
				try {
					SECONDS.sleep(12);
					log.info("[{}] Take a message...", SEQ.getAndIncrement());
					String message = queue.take();
					log.info("[{}] Received message: " + message, SEQ.getAndIncrement());
					log.info("[{}] sleep 3 seconds", SEQ.getAndIncrement());
					SECONDS.sleep(3);
					message = queue.take();
					log.info("[{}] Take another message: " + message, SEQ.getAndIncrement());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}.start();

		try {

			log.info("[{}] Try putting message: Hello World!", SEQ.getAndIncrement());
			queue.put("Hello World!");
			log.info("[{}] Hello World! Message put!", SEQ.getAndIncrement());
			log.info("[{}] Try putting message again ", SEQ.getAndIncrement());
			queue.put("Hello Blocking queue!");
			log.info("[{}] Message sent again!", SEQ.getAndIncrement());
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
