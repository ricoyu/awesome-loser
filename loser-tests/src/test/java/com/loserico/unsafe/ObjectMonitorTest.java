package com.loserico.unsafe;

import com.loserico.common.lang.magic.UnsafeInstance;
import sun.misc.Unsafe;

/**
 * <p>
 * Copyright: (C), 2019/11/22 12:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ObjectMonitorTest {
	
	private static Object object = new Object();
	
	public static void main(String[] args) {
		Unsafe unsafe = UnsafeInstance.get();
		//unsafe.monitorEnter(object);
		System.out.println("这里写业务逻辑");
		//unsafe.monitorExit(object);
	}
}
