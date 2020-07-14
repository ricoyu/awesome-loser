package com.loserico.pattern.structural.flightweight;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * Copyright: (C), 2020/2/7 12:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class FlyweightFactory {
	
	private ConcurrentMap<String, Flyweight> files = new ConcurrentHashMap<>();
	
	public Flyweight factory(String internalState) {
		Flyweight flyweight = files.get(internalState);
		if (flyweight == null) {
			flyweight = new ConcreteFlyweight(internalState);
			files.put(internalState, flyweight);
		}
		
		return flyweight;
	}
	
	public int getFlyWeightSize() {
		return files.size();
	}
}
