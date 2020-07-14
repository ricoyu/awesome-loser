package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OutForDelivery implements PackageState {
	
	private static final OutForDelivery instance = new OutForDelivery();
	
	public static OutForDelivery instance() {
		return instance;
	}
	
	@Override
	public void updateState(DeliveryContext ctx) {
		System.out.println("Package is out of delivery !!");
		ctx.setCurrentState(DeliveredState.instance());
	}
}
