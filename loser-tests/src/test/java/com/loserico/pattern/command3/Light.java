package com.loserico.pattern.command3;

//Receiver
public class Light {

	private boolean on;

	public void switchOn() {
		on = true;
	}

	public void switchOff() {
		on = false;
	}
}