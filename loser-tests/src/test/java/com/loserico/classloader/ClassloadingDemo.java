package com.loserico.classloader;

/**
 * <p>
 * Copyright: (C), 2023-02-27 9:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClassloadingDemo {
	
	public static void main(String[] args) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		System.out.println(classLoader);
	}
}
