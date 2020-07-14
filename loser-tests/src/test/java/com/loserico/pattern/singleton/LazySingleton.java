package com.loserico.pattern.singleton;

/**
 * 当被问到要实现一个单例模式时, 很多人的第一反应是写出如下的代码, 包括教科书上也是这样教我们的。
 * 
 * 这段代码简单明了, 而且使用了懒加载模式, 但是却存在致命的问题。当有多个线程并行调用 getInstance()
 * 的时候, 就会创建多个实例。也就是说在多线程下不能正常工作。
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class LazySingleton {
	private static LazySingleton instance;

	private LazySingleton() {
	}

	public static LazySingleton getInstance() {
		if (instance == null) {
			instance = new LazySingleton();
		}
		return instance;
	}
}