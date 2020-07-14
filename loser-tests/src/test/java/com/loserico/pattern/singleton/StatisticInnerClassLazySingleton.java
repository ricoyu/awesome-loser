package com.loserico.pattern.singleton;

/**
 * 由于对象实例化是在内部类加载的时候构建的, 因此该版是线程安全的
 * (因为在方法中创建对象, 才存在并发问题, 静态内部类随着方法调用而被加载, 只加载一次, 不存在并发问题, 所以是线程安全的)
 * 
 * 另外, 在getInstance()方法中没有使用synchronized关键字, 因此没有造成多余的性能损耗。
 * 当StatisticInnerClassSingleton类加载的时候, 其静态内部类SingletonHolder并没有被加载, 因此instance对象并没有构建。
 * 
 * 而我们在调用StatisticInnerClassSingleton.getInstance()方法时, 内部类SingletonHolder被加载, 此时单例对象才被构建。
 * 因此, 这种写法节约空间, 达到懒加载的目的
 * 
 * <p>
 * Copyright: Copyright (c) 2018-05-05 19:49
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StatisticInnerClassLazySingleton {

	private static class SingletonHolder {
		private static final StatisticInnerClassLazySingleton instance = new StatisticInnerClassLazySingleton();
	}

	private StatisticInnerClassLazySingleton() {
	}

	public static final StatisticInnerClassLazySingleton getInstance() {
		return SingletonHolder.instance;
	}
}