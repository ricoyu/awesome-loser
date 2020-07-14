package com.loserico.pattern.structural.decorator;

/**
 * 摩卡是一个装饰者
 * <p>
 * Copyright: (C), 2020/2/14 9:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Mocha extends CondimentDecorator {
	
	private Beverage beverage;
	
	public Mocha(Beverage beverage) {
		this.beverage = beverage;
	}
	
	public String getDescription() {
		return beverage.getDescription() + ", Mocha";
	}
	
	@Override
	public double cost() {
		return 0.20 + beverage.cost();
	}
}
