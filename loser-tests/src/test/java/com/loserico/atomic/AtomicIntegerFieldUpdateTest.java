package com.loserico.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * <p>
 * Copyright: (C), 2019/11/22 10:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AtomicIntegerFieldUpdateTest {
	
	static AtomicIntegerFieldUpdater aifu = AtomicIntegerFieldUpdater.newUpdater(Student.class, "old");
	
	public static void main(String[] args) {
		Student student = new Student("三少爷", 18);
		System.out.println(aifu.getAndIncrement(student));
		System.out.println(aifu.get(student));
		System.out.println(student.getOld());
	}
	
	static class Student {
		private String name;
		public volatile int old;
		
		public Student(String name, int old) {
			this.name = name;
			this.old = old;
		}
		
		public String getName() {
			return name;
		}
		
		public int getOld() {
			return old;
		}
	}
}
