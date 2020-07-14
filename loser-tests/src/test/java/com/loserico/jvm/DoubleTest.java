package com.loserico.jvm;

public class DoubleTest {

	static double doubleValue(double a) {
		return a * 2;
	}
	
	public static void main(String[] args) {
		double a = 200000d;
		for (int i = 0; i < 10; i++) {
			a = doubleValue(a);
		}
		
		System.out.println(a);
	}
}
