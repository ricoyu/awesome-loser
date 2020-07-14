package com.loserico.pattern.structural.adapter;

/**
 * 鸟类接口
 * <p>
 * Copyright: (C), 2020/2/15 12:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Bird {
	
	/**
	 * birds implement Bird interface that allows them to fly and make sounds adaptee interface
	 */
	public void fly();
	
	public void makeSound();
}
