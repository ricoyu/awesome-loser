package com.loserico.pattern.singleton;

import java.io.Serializable;

public class LazySingleton4 implements Serializable {
	private static boolean initialized = false;

	private LazySingleton4() {
		synchronized (LazySingleton4.class) {
			if (initialized == false) {
				initialized = !initialized;
			} else {
				throw new RuntimeException("单例已被破坏");
			}
		}
	}

	static class SingletonHolder {
		private static final LazySingleton4 instance = new LazySingleton4();
	}

	public static LazySingleton4 getInstance() {
		return SingletonHolder.instance;
	}

	private Object readResolve() {
		return getInstance();
	}
}