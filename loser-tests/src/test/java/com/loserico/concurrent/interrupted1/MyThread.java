package com.loserico.concurrent.interrupted1;

public class MyThread extends Thread {
	public void run() {
		super.run();
		for (int i = 0; i < 500000; i++) {
			System.out.println("i=" + (i + 1));
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("我被打断了, 我要罢工!");
				break;
			}
		}
	}
}
