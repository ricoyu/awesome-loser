package com.loserico.jvm.deadlock;

public class Math {
	
	public static final int initData = 666;
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 14; i++) {
			new Thread(() -> {
				Math math = new Math();
				while (true) {
					math.compute();
				}
				
			}).start();
		}
		
		Thread.currentThread().join();
	}
	
	public int compute() {
		int a = 1;
		int b = 2;
		int c = (a + b) * 10;
		return c;
	}
	
}
