package com.loserico.jvm;

public class ReferenceCountingGC {

	public Object instance = null;

	private static final int _1MB = 2014 * 1024;

	/*
	 * 这个成员属性的唯一意义就是占点内存, 以便能在GC日志中看清楚是否被回收过
	 */
	private byte[] bigSize = new byte[2 * _1MB];

	public static void testGC() {
		ReferenceCountingGC objA = new ReferenceCountingGC();
		ReferenceCountingGC objB = new ReferenceCountingGC();

		objA.instance = objB;
		objB.instance = objA;

		// 假设在这行发生GC, objA和objB是否能被回收?
		System.gc();
	}
	
	public static void main(String[] args) {
		ReferenceCountingGC.testGC();
	}
}
