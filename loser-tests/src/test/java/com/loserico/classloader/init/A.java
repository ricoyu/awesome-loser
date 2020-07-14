package com.loserico.classloader.init;

public class A {
	
	private static String name = "abc";

	static {
		System.out.println("Initialize A static block");
	}
	
	public static String getName() {
		return name;
	}
}
