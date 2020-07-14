package com.loserico.pattern.behavioral.mediator1.mediator;

import com.loserico.pattern.behavioral.mediator1.Motor;
import com.loserico.pattern.behavioral.mediator1.Sensor;
import com.loserico.pattern.behavioral.mediator1.SoilRemoval;
import com.loserico.pattern.behavioral.mediator1.colleague.Heater;
import com.loserico.pattern.behavioral.mediator1.colleague.Machine;
import com.loserico.pattern.behavioral.mediator1.colleague.Valve;

/**
 * In order to feel the advantages and power of the Mediator Pattern, let’s take
 * another mediator that is used as a washing program for denims. Now we just need to
 * create a new mediator and set the rules in it to wash denims: rules like
 * temperature, and the speed at which drum will spin, whether softener is required or
 * not, the level of the soil removal, etc. We don’t need to change anything in the
 * existing structure. No conditional statements like “if-else” are required,
 * something that would increase the complexity.
 * 
 * You can clearly see the differences between the two mediator classes. There is
 * different temperature, spinning speed is also different and no softener is required
 * for the denim wash.
 * 
 * @author Rico Yu
 * @since 2017-01-10 15:05
 * @version 1.0
 *
 */
public class DenimMediator implements Mediator {

	private final Machine machine;
	private final Heater heater;
	private final Motor motor;
	private final Sensor sensor;
	private final SoilRemoval soilRemoval;
	private final Valve valve;

	public DenimMediator(Machine machine, Heater heater, Motor motor, Sensor sensor, SoilRemoval soilRemoval,
			Valve valve) {
		this.machine = machine;
		this.heater = heater;
		this.motor = motor;
		this.sensor = sensor;
		this.soilRemoval = soilRemoval;
		this.valve = valve;

		System.out.println(".........................Setting up for DENIM program.........................");
	}

	@Override
	public void start() {
		machine.start();
	}

	@Override
	public void wash() {
		motor.startMotor();
		motor.rotateDrum(1400);
		System.out.println("Adding detergent");
		soilRemoval.medium();
		System.out.println("No softener is required");
	}

	@Override
	public void open() {
		valve.open();
	}

	@Override
	public void closed() {
		valve.closed();
	}

	@Override
	public void on() {
		heater.on(30);
	}

	@Override
	public void off() {
		heater.off();
	}

	@Override
	public boolean checkTemperature(int temp) {
		return sensor.checkTemperature(temp);
	}

}