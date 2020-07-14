package com.loserico.collections.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerDirector {

	public static void main(String[] args) {
		BlockingQueue<Long> q = new ArrayBlockingQueue<>(1024);
		Producer p = new Producer(q);
		new Thread(p).start();
	}
}
