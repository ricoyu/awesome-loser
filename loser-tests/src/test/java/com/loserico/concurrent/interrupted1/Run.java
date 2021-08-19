package com.loserico.concurrent.interrupted1;

public class Run {
	public static void main(String args[]) {
		Thread thread = new MyThread();
		thread.start();
		try {
			Thread.sleep(2000);
			thread.interrupt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
