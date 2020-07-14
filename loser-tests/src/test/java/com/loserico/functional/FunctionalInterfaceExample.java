package com.loserico.functional;

@FunctionalInterface
public interface FunctionalInterfaceExample {
	void apply();

}

interface B extends FunctionalInterfaceExample {
//	 void illegal();
}

