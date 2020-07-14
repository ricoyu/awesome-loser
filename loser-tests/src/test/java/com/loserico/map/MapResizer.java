package com.loserico.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Copyright: (C), 2019/11/24 14:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MapResizer implements Runnable {
	
	private static Map<Integer, Integer> map = new HashMap<>(2);
	
	private static AtomicInteger atomicInteger = new AtomicInteger();
	
	@Override
	public void run() {
		while (atomicInteger.get() < 100000) {
			map.put(atomicInteger.get(), atomicInteger.get());
			atomicInteger.incrementAndGet();
		}
	}
}
