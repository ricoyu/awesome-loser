package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeliveredState implements PackageState {
	
	private static final DeliveredState instance = new DeliveredState();
	
	public static DeliveredState instance() {
		return instance;
	}
	
	private DeliveredState() {
		
	}
	
	//Business logic
	@Override
	public void updateState(DeliveryContext ctx) {
		System.out.println("Package is delivered !!");
	}
}
