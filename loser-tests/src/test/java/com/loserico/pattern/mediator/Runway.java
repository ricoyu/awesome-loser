package com.loserico.pattern.mediator;

/**
 * <p>
 * Copyright: (C), 2020/1/30 9:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Runway implements Command {
	
	private Mediator mediator;
	
	public Runway(Mediator mediator) {
		this.mediator = mediator;
	}
	
	@Override
	public void land() {
		System.out.println("Landing permission granted.");
		mediator.setLandingStatus(true);
	}
}
