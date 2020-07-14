package com.loserico.concurrent.lock;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2019/11/18 11:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LockEliminate {
	
	public void test() {
		/*
		 * 进过逃逸分析, new Object()不会被当前线程外的线程访问, 
		 * 加锁无意义, 所以JVM经过优化, 实际不会对这段代码加锁
		 */
		synchronized (new Object()) {
			//.....
			System.out.println(1);
		}
	}
	
	public static void main(String[] args) {
		LockEliminate lockEliminate = new LockEliminate();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 100000; j++) {
					lockEliminate.test();
				}
			}).start();
		}
		
		try {
			TimeUnit.MILLISECONDS.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
