package com.loserico.pattern.flyweight.demo;

public class CPPPlatform implements Platform {
	public CPPPlatform() {
		System.out.println("CPPPlatform object created");
	}

	@Override
	public void execute(Code code) {
		System.out.println("Compiling and executing CPP code.");
	}
}