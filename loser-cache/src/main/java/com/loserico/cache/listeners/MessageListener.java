package com.loserico.cache.listeners;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-17 17:05
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface MessageListener {

	/**
	 * Redis发布/订阅模式支持
	 * @param channel
	 * @param message
	 */
	void onMessage(String channel, String message);
}
