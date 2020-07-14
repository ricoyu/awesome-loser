package com.loserico.pattern.structural.adapter;

/**
 * <p>
 * Copyright: (C), 2020/2/15 12:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PlasticToyDuck implements ToyDuck {
	
	@Override
	public void squeak() {
		System.out.println("Squeak");
	}
}
