package com.loserico.threadlocal;

/**
 * <p>
 * Copyright: (C), 2023-02-13 17:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadLocalTest {
	
	private static ThreadLocal<String> s = new ThreadLocal<>();
	
	public static void main(String[] args) {
		s.set("ricoyu");
		System.out.println(s.get());
		s.remove();
	}
}
