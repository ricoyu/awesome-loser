package com.loserico.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Copyright: (C), 2019/11/25 18:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConcurrentHashMapTest {
	
	public static void main(String[] args) {
		ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
		concurrentHashMap.put("k", "v");
	}
}
