package com.loserico.concurrent;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 模拟CAS计数器
 * <p>
 * Copyright: Copyright (c) 2019-01-22 17:49
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@ThreadSafe
public class CasCounter {
	private SimulatedCAS value;

	public int getValue() {
		return value.get();
	}

	public int increment() {
		int v;
		do {
			v = value.get();
		} while (v != value.compareAndSwap(v, v + 1));

		return v + 1;
	}
}