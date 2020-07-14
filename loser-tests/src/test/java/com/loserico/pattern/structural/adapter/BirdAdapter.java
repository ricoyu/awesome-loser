package com.loserico.pattern.structural.adapter;

/**
 * 要实现客户端期望的接口
 * <p>
 * Copyright: (C), 2020/2/15 12:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BirdAdapter implements ToyDuck {
	
	private Bird bird;
	
	public BirdAdapter(Bird bird) {
		this.bird = bird;
	}
	
	@Override
	public void squeak() {
		bird.makeSound();
	}
}
