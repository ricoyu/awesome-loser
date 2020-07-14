package com.loserico.collections.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConsumerDirector {
	public static void main(String[] args) {
		BlockingQueue<Long> q = new ArrayBlockingQueue<>(1024);
		Consumer c2 = new Consumer(q);
		new Thread(c2).start();
	}
}
