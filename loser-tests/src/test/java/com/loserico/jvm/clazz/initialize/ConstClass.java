package com.loserico.jvm.clazz.initialize;

public class ConstClass {

	static {
		System.out.println("ConstClass init!");
	}
	
	public static final String HELLO_WORLD = "hello world!";
}
