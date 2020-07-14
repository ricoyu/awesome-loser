package com.loserico.pattern.behavioral.state;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PackageDeliveryStateDemo {
	
	public static void main(String[] args) {
		DeliveryContext ctx = new DeliveryContext(null, "Test123");
		
		ctx.update();
		ctx.update();
		ctx.update();
		ctx.update();
		ctx.update();
	}
}
