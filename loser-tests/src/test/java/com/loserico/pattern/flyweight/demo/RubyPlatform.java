package com.loserico.pattern.flyweight.demo;

public class RubyPlatform implements Platform {
	public RubyPlatform() {
		System.out.println("RubyPlatform object created");
	}

	@Override
	public void execute(Code code) {
		System.out.println("Compiling and executing Ruby code.");
	}
}