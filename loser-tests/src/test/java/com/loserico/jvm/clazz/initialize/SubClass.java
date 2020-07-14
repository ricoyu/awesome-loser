package com.loserico.jvm.clazz.initialize;

public class SubClass extends SuperClass {

	static {
		System.out.println("Subclass init");
	}
}
