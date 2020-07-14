package com.loserico.pattern.behavioral.mediator1;

public class Sensor {

	public boolean checkTemperature(int temp) {
		System.out.println("Temperature reached " + temp + " *C");
		return true;
	}

}