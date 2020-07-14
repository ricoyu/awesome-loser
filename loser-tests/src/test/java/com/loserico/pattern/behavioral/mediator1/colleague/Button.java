package com.loserico.pattern.behavioral.mediator1.colleague;

import com.loserico.pattern.behavioral.mediator1.mediator.Mediator;

/**
 * The Button class is a colleague class which holds a reference to a mediator.
 * The user press the button which calls the press() method of this class which in
 * turn, calls the start() method of the concrete mediator class. This start() method
 * of the mediator calls the start() method of machine class on behalf of the Button
 * class.
 * 
 * @author Rico Yu
 * @since 2017-01-09 21:37
 * @version 1.0
 *
 */
public class Button implements Colleague {

	private Mediator mediator;

	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}

	public void press() {
		System.out.println("Button pressed.");
		mediator.start();
	}

}
