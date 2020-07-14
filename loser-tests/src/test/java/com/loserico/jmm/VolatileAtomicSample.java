package com.loserico.jmm;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2019/11/13 14:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class VolatileAtomicSample {
	
	private static volatile int counter = 0;
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					counter++;
				}
			});
			thread.start();
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(counter);
	}
}
