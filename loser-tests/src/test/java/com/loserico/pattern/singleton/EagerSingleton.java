package com.loserico.pattern.singleton;

/**
 * 优点就是线程安全, 缺点很明显, 类加载的时候就实例化对象了, 浪费空间。于是乎, 就提出了懒汉式的单例模式
 * 
 * <p>
 * Copyright: Copyright (c) 2018-05-05 19:43
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class EagerSingleton {
	// 类加载时就初始化
	private static final EagerSingleton instance = new EagerSingleton();

	private EagerSingleton() {
	}

	public static EagerSingleton getInstance() {
		return instance;
	}
}