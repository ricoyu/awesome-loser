package com.loserico.pattern.behavioral.mediator1.colleague;

import com.loserico.pattern.behavioral.mediator1.mediator.Mediator;

/**
 * The heater’s on() method switch on the heater and set the temperature as required.
 * It also checks if temperature is reached as required, it turns off() the method.
 * The checking of the temperature and switching off the heater is done through the
 * mediator. After switching off, it calls the wash() method of the Machine class
 * through the mediator to start washing.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:41
 * @version 1.0
 *
 */
public class Heater implements Colleague {

	private Mediator mediator;

	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}

	public void on(int temp) {
		System.out.println("Heater is on...");
		if (mediator.checkTemperature(temp)) {
			System.out.println("Temperature is set to " + temp);
			mediator.off();
		}
	}

	public void off() {
		System.out.println("Heater is off...");
		mediator.wash();
	}
}
