package com.loserico.concurrent.lock;

/**
 * <p>
 * Copyright: (C), 2019/11/17 14:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SynchronizedClassFileSample {
	
	public void lock() {
		synchronized (this) {
			System.out.println("do in synchronized");
		}
	}
}
