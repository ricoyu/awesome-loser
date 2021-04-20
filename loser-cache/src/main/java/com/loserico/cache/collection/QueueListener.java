package com.loserico.cache.collection;

/**
 * Redis阻塞队列监听器
 * <p>
 * Copyright: Copyright (c) 2018-08-01 21:50
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface QueueListener {

	/**
	 * 
	 * @param key
	 * @param message
	 */
	public void onDeque(String key, Object message);
}
