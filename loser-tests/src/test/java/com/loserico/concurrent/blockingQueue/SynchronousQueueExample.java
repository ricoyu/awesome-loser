package com.loserico.concurrent.blockingQueue;

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueExample {
	
	public static void main(String[] args) {
		SynchronousQueue<String> queue = new SynchronousQueue<>();
		
		Thread producer = new Thread(() -> {
			try {
				System.out.println("Putting message: Hello World!");
				Thread.currentThread().sleep(1000);
				queue.put("Hello World!");
				System.out.println("Message sent!");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		
		Thread consumer = new Thread(() -> {
			try {
				System.out.println("Waiting for a message...");
				String message = queue.take();
				System.out.println("Received message: " + message);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		
		producer.start();
		consumer.start();
	}
}
