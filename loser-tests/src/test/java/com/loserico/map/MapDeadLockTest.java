package com.loserico.map;

/**
 * <p>
 * Copyright: (C), 2019/11/24 14:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MapDeadLockTest {
	
	/**
	 * JDK1.7的HashMap会产生死锁
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			new Thread(new MapResizer()).start();
		}
	}
}
