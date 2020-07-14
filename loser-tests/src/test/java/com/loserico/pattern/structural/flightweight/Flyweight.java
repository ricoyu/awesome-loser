package com.loserico.pattern.structural.flightweight;

/**
 * 抽象蝇量角色
 * 
 * <p>
 * Copyright: (C), 2020/2/7 12:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Flyweight {
	
	/**
	 * 
	 * @param externalState 外部状态
	 */
	public void operation(String externalState);
}
