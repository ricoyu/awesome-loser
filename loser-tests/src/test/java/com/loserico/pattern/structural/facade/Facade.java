package com.loserico.pattern.structural.facade;

/**
 * <p>
 * Copyright: (C), 2020/2/12 10:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Facade {
	
	private Light light1, light2, light3;
	private Heater heater;
	private TV tv;
	
	public Facade() {
		this.light1 = new Light();
		this.light2 = new Light();
		this.light3 = new Light();
		this.heater = new Heater();
		this.tv = new TV();
	}
	
	public void open() {
		light1.open();
		light2.open();
		light3.open();
		heater.open();
		tv.open();
	}
}
