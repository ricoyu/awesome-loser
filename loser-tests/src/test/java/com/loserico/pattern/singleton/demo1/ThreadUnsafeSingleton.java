package com.loserico.pattern.singleton.demo1;

/**
 * 种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。
 * 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。
 * <p>
 * Copyright: (C), 2020/6/30 15:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadUnsafeSingleton {
	
	private static ThreadUnsafeSingleton instance;
	
	private ThreadUnsafeSingleton() {
	}
	
	public static ThreadUnsafeSingleton getInstance() {
		if (instance == null) {
			instance = new ThreadUnsafeSingleton();
		}
		
		return instance;
	}
}
