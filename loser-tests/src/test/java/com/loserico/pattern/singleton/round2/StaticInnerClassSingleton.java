package com.loserico.pattern.singleton.round2;

public class StaticInnerClassSingleton {

	private StaticInnerClassSingleton(){}

	private static class SingletonHolder {
		private static StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
	}

	public static StaticInnerClassSingleton getInstance() {
		return SingletonHolder.instance;
	}
}
