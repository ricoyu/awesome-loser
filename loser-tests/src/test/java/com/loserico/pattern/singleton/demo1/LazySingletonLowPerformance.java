package com.loserico.pattern.singleton.demo1;

/**
 * 这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步。
 * <p>
 * Copyright: (C), 2020/6/30 15:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LazySingletonLowPerformance {
	
	private static LazySingletonLowPerformance instance;
	
	private LazySingletonLowPerformance() {
	}
	
	public static synchronized LazySingletonLowPerformance getInstance() {
		if (instance == null) {
			instance = new LazySingletonLowPerformance();
		}
		
		return instance;
	}
}
