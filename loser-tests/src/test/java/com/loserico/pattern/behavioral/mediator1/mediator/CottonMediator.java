package com.loserico.pattern.behavioral.mediator1.mediator;

import com.loserico.pattern.behavioral.mediator1.Motor;
import com.loserico.pattern.behavioral.mediator1.Sensor;
import com.loserico.pattern.behavioral.mediator1.SoilRemoval;
import com.loserico.pattern.behavioral.mediator1.colleague.Heater;
import com.loserico.pattern.behavioral.mediator1.colleague.Machine;
import com.loserico.pattern.behavioral.mediator1.colleague.Valve;

/**
 * @of
 * As stated by the company, the washing machine has a set of wash programs and the
 * software should support all these programs. 
 * 
 * The below mediator is actually one of the washing programs for the machine. 
 * The below mediator is set as a washing program for cottons, 
 * so parameters such as temperature, drum spinning speed, level of soil removal etc., are set accordingly. 
 * We can have different mediators for different washing programs without changing the existing colleague classes 
 * and thus providing loose coupling and reusability. 
 * 
 * All these colleague classes can be reused with others washing programs of the machine.
 * 
 * The CottonMediator class implements the MachineMediator interface and provides the required methods. 
 * These methods are the operations that are performed by the colleague objects in order to get the work done. 
 * The above mediator class just calls the method of a colleague object on behalf of another colleague object in order to achieve this.
 * 
 * @on
 * @author Rico Yu
 * @since 2017-01-09 21:45
 * @version 1.0
 *
 */
public class CottonMediator implements Mediator {

	// 这个三个是Colleague的子类
	private final Machine machine;
	private final Heater heater;
	private final Valve valve;

	// 这三个属于功能性的帮助类，不在中介者模式的角色之内
	private final Motor motor;
	private final Sensor sensor;
	private final SoilRemoval soilRemoval;

	public CottonMediator(Machine machine, Heater heater, Motor motor, Sensor sensor, SoilRemoval soilRemoval,
			Valve valve) {
		this.machine = machine;
		this.heater = heater;
		this.valve = valve;

		this.motor = motor;
		this.sensor = sensor;
		this.soilRemoval = soilRemoval;

		System.out.println(".........................Setting up for COTTON program.........................");
	}

	@Override
	public void start() {
		machine.start();
	}

	@Override
	public void wash() {
		motor.startMotor();
		motor.rotateDrum(700);
		System.out.println("Adding detergent");
		soilRemoval.low();
		System.out.println("Adding softener");
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
		heater.on(40);
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