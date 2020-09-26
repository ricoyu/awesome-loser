package com.loserico.concurrent.lock;

/**
 * <p>
 * Copyright: (C), 2020-09-26 20:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SynchronizedMethod {
	
	public synchronized void hi() {
		System.out.println("Hello World");
	}
}
