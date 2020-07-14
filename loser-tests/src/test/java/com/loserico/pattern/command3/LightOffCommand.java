package com.loserico.pattern.command3;

//Concrete Command
public class LightOffCommand implements Command {
	// reference to the light
	Light light;

	public LightOffCommand(Light light) {
		this.light = light;
	}

	public void execute() {
		light.switchOff();
	}
}