package com.loserico.atomic;

import com.loserico.common.lang.magic.UnsafeInstance;
import sun.misc.Unsafe;

/**
 * <p>
 * Copyright: (C), 2019/11/22 10:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AtomicStudentAgeUpdater {
	
	private String name;
	
	private volatile int age;
	
	private static final Unsafe unsafe = UnsafeInstance.get();
	private static final long valueOffset;
	
	static {
		try {
			valueOffset = unsafe.objectFieldOffset(AtomicStudentAgeUpdater.class.getDeclaredField("age"));
			System.out.println("valueOffset--->" + valueOffset);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public AtomicStudentAgeUpdater(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public void compareAndSwapAge(int old, int target) {
		unsafe.compareAndSwapInt(this, valueOffset, old, target);
	}
	
	public int getAge() {
		return this.age;
	}
	
	public static void main(String[] args) {
		AtomicStudentAgeUpdater updater = new AtomicStudentAgeUpdater("三少爷", 18);
		updater.compareAndSwapAge(18, 37);
		
		System.out.println("真实的三少爷年龄---" + updater.getAge());
	}
}
