package com.loserico;

import sun.misc.Unsafe;

/**
 * <p>
 * Copyright: (C), 2019/11/20 13:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnsafeTest2 {
	
	public static void main(String[] args) {
		Unsafe unsafe = Unsafe.getUnsafe();
		Object object = new Object();
		//unsafe.monitorEnter(object);
		System.out.println("............");
		System.out.println(UnsafeTest.class.getClassLoader());
		//unsafe.monitorExit(object);
	}
}
