package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AcknowledgedState implements PackageState {
	
	//Singleton
	private static final AcknowledgedState instance = new AcknowledgedState();
	
	private AcknowledgedState() {
		
	}
	
	public static AcknowledgedState instance() {
		return instance;
	}
	
	//Business logic and state transition
	@Override
	public void updateState(DeliveryContext ctx) {
		System.out.println("Package is acknowledged !!");
		ctx.setCurrentState(Shipped.instance());
	}
}
