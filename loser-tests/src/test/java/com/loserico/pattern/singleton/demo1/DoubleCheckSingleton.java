package com.loserico.pattern.singleton.demo1;

/**
 * 这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 * <p>
 * Copyright: (C), 2020/6/30 15:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DoubleCheckSingleton {
	
	private static volatile DoubleCheckSingleton instance;
	
	private DoubleCheckSingleton() {
	}
	
	public static DoubleCheckSingleton getInstance() {
		if (instance == null) {
			synchronized (DoubleCheckSingleton.class) {
				if (instance == null) {
					instance = new DoubleCheckSingleton();
				}
			}
		}
		
		return instance;
	}
}
