package com.loserico.classloader.init;

public class D {

	private static volatile D d = new D();
	private static int a = 0;
	
	private static int b;
	
	private D() {
		a++;
		b++;
	}
	
	public static D getInstance() {
		return d;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}
	
}
