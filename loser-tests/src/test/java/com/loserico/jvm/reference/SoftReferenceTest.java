package com.loserico.jvm.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试软引用
 * <p>
 * Copyright: Copyright (c) 2019-08-06 15:23
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class SoftReferenceTest {

	private static ReferenceQueue<User> queue = new ReferenceQueue<>();
	
	private static void printQueue() {
		Reference<? extends User> ref = queue.poll();
		if (ref !=  null) {
			System.out.println("the gc object reference == " + ref.get());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		List<SoftReference<User>> srUsers = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			SoftReference<User> sr = new SoftReference<User>(new User("soft " + i), queue);
			System.out.println("now the user==" + sr.get());
			
			srUsers.add(sr);
		}
		
		System.gc();
		Thread.sleep(1000);
		printQueue();
	}
}
