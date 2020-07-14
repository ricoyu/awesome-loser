package com.loserico.pattern.structural.decorator;

/**
 * <p>
 * Copyright: (C), 2020/2/14 9:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HouseBlend extends Beverage {
	
	public HouseBlend() {
		description = "House Belend Coffee";
	}
	
	@Override
	public double cost() {
		return 0.89;
	}
}
