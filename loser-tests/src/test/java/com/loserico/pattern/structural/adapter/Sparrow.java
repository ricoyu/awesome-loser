package com.loserico.pattern.structural.adapter;

/**
 * 麻雀实现了Bird接口
 * <p>
 * Copyright: (C), 2020/2/15 12:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Sparrow implements Bird {
	
	@Override
	public void fly() {
		System.out.println("Flying");
	}
	
	@Override
	public void makeSound() {
		System.out.println("Chirp Chirp");
	}
}
