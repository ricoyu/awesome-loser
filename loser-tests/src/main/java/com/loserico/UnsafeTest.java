package com.loserico;

import sun.misc.Unsafe;

public class UnsafeTest {
	
	public static void main(String[] args) {
		Unsafe unsafe = Unsafe.getUnsafe();
		Object object = new Object();
		unsafe.monitorEnter(object);
		System.out.println("............");
		System.out.println(UnsafeTest.class.getClassLoader() == null ? "BootstrapClassLoader" : UnsafeTest.class.getClassLoader());
		unsafe.monitorExit(object);
	}
}