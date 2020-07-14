package com.loserico.pattern.structural.decorator;

/**
 * <p>
 * Copyright: (C), 2020/2/14 10:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StarbuzzCoffee {
	
	public static void main(String[] args) {
		Beverage beverage = new Expresso();
		System.out.println(beverage.getDescription() + "$" + beverage.cost());
		
		Beverage beverage1 = new DarkRost();
		System.out.println(beverage1.getDescription() + "$" + beverage1.cost());
		
		beverage1 = new Mocha(beverage1);
		beverage1 = new Mocha(beverage1);
		System.out.println(beverage1.getDescription() + "$" + beverage1.cost());
	}
}
