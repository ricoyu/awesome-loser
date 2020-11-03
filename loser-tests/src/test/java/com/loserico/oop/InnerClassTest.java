package com.loserico.oop;

/**
 * <p>
 * Copyright: (C), 2020-10-24 12:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InnerClassTest {
	
	private String name = "三少爷";
	
	private Unsafe unsafe;
	
	public String getName() {
		return this.name;
	}
	
	public void newUnsafe() {
		this.unsafe = new Unsafe();
	}
	
	private class Unsafe{
		
		public void print() {
			System.out.println(InnerClassTest.this.name);
			System.out.println(getName());
		}
		
	}
	
	public static void main(String[] args) {
		InnerClassTest innerClassTest = new InnerClassTest();
		innerClassTest.newUnsafe();
		innerClassTest.unsafe.print();
	}
}
