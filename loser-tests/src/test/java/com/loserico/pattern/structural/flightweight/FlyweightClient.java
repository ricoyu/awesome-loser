package com.loserico.pattern.structural.flightweight;

/**
 * <p>
 * Copyright: (C), 2020/2/7 12:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FlyweightClient {
	
	public static void main(String[] args) {
		FlyweightFactory factory = new FlyweightFactory();
		Flyweight flyweight1 = factory.factory("辣椒炒肉");
		flyweight1.operation("三少爷点菜");
		
		Flyweight flyweight2 = factory.factory("回锅肉");
		flyweight1.operation("Sexy Uncle 点菜");
		
		Flyweight flyweight3 = factory.factory("回锅肉");
		flyweight1.operation("仔仔点菜");
		
		System.out.println(flyweight2 == flyweight3);
		System.out.println("被点不同的菜的个数 " + factory.getFlyWeightSize());
	}
}
