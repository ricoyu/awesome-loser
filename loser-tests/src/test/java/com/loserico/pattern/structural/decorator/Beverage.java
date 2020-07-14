package com.loserico.pattern.structural.decorator;

/**
 * 这是我们的抽象组件类 饮料
 * <p>
 * Copyright: (C), 2020/2/14 9:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class Beverage {
	
	protected String description = "Unknown Beverage";
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * 计算饮料价格
	 * @return
	 */
	public abstract double cost();
}
