package com.loserico.jvm.clazz.initialize;

public class DeadLoopClass {

	static {
		if (true) {
			System.out.println(Thread.currentThread() + "init DeadLoopClass");
			while (true) {
				
			}
		}
	}
	
	public static void main(String[] args) {
		Runnable script = () -> {
			System.out.println(Thread.currentThread() + " start");
			DeadLoopClass dlClass = new DeadLoopClass();
			System.out.println(Thread.currentThread() + "ru over");
		};
		
		Thread thread1 = new Thread(script);
		Thread thread2 = new Thread(script);
		thread1.start();
		thread2.start();
	}
}
