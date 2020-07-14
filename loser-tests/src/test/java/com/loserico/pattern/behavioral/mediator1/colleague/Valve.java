package com.loserico.pattern.behavioral.mediator1.colleague;

import com.loserico.pattern.behavioral.mediator1.mediator.Mediator;

/**
 * The Valve class has two methods, an open() method which is called to open the valve
 * and when the water is filled it called the closed() method. But please note that it
 * is not calling the closed() method directly, it calls the closed() method of the
 * mediator which invokes the method of this class. On closing the valve it turns the
 * heater on but again by invoking the mediator’s method instead of directly calling
 * the heater’s method.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:40
 * @version 1.0
 *
 */
public class Valve implements Colleague {

	private Mediator mediator;

	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}

	public void open() {
		System.out.println("Valve is opened...");
		System.out.println("Filling water...");
		mediator.closed();
	}

	public void closed() {
		System.out.println("Valve is closed...");
		mediator.on();
	}
}
