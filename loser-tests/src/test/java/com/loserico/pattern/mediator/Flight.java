package com.loserico.pattern.mediator;

/**
 * <p>
 * Copyright: (C), 2020/1/30 9:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Flight implements Command {
	
	private Mediator mediator;
	
	public Flight(Mediator mediator) {
		this.mediator = mediator;
	}
	
	@Override
	public void land() {
		if (mediator.isLandingOk()) {
			System.out.println("Successfully Landed.");
			mediator.setLandingStatus(true);
		} else {
			System.out.println("Waiting for landing.");
		}
	}
	
	public void getReady() {
		System.out.println("Ready for landing.");
	}
}
