package com.loserico.classloader.init;

public class C extends A {

	static {
		System.out.println("initialize B in static block");
	}
}
