package com.loserico.pattern.command3;

//Client
public class Client {

	public static void main(String[] args) {
		RemoteControl control = new RemoteControl(); // Invoker
		Light light = new Light(); // Receiver
		Command lightsOn = new LightOnCommand(light);
		Command lightsOff = new LightOffCommand(light);
		// switch on
		control.setCommand(lightsOn);
		control.pressButton();
		// switch off
		control.setCommand(lightsOff);
		control.pressButton();
	}
}