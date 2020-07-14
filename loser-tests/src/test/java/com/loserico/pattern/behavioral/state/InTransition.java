package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InTransition implements PackageState {
	
	private static final InTransition instance = new InTransition();
	
	public static InTransition instance() {
		return instance;
	}
	
	private InTransition() {
		
	}
	
	@Override
	public void updateState(DeliveryContext ctx) {
		System.out.println("Package is in transition !!");
		ctx.setCurrentState(OutForDelivery.instance());
	}
}
