package com.loserico.pattern.behavioral.mediator1.colleague;

import com.loserico.pattern.behavioral.mediator1.mediator.Mediator;

/**
 * The Machine class which hold a reference to the mediator has the start()
 * method which is called on the press of the button by the mediator class as
 * discussed above. The method has an open() method of the mediator which in turn
 * calls the open() method of the Valve class in order to open the valve of the
 * machine.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:39
 * @version 1.0
 *
 */
public class Machine implements Colleague {

	private Mediator mediator;

	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}

	public void start() {
		mediator.open();
	}

	public void wash() {
		mediator.wash();
	}
}