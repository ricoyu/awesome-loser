package com.loserico.pattern.structural.decorator;

/**
 * <p>
 * Copyright: (C), 2020/2/14 9:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Expresso extends Beverage {
	
	public Expresso() {
		description = "Espresso";
	}
	
	@Override
	public double cost() {
		return 1.99;
	}
}
