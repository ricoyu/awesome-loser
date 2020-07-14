package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Shipped implements PackageState {
	
	private static final Shipped instance = new Shipped();
	
	public static Shipped instance() {
		return instance;
	}
	
	private Shipped() {
		
	}
	
	@Override
	public void updateState(DeliveryContext ctx) {
		System.out.println("Package is shipped !!");
		ctx.setCurrentState(InTransition.instance());
	}
}
