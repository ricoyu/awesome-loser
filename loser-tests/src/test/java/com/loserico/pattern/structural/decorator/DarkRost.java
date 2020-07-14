package com.loserico.pattern.structural.decorator;

/**
 * <p>
 * Copyright: (C), 2020/2/14 10:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DarkRost extends Beverage {
	
	public DarkRost() {
		description = "Dark Rost Coffee";
	}
	
	@Override
	public double cost() {
		return 1.66;
	}
}
