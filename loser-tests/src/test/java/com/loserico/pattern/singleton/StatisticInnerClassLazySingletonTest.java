package com.loserico.pattern.singleton;

import java.lang.reflect.Constructor;

public class StatisticInnerClassLazySingletonTest {

	public static void main(String[] args) {
		//创建第一个实例
		StatisticInnerClassLazySingleton instance1 = StatisticInnerClassLazySingleton.getInstance();
		//通过反射创建第二个实例
		StatisticInnerClassLazySingleton instance2 = null;
		try {
			Class<StatisticInnerClassLazySingleton> clazz = StatisticInnerClassLazySingleton.class;
			Constructor<StatisticInnerClassLazySingleton> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			instance2 = constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		* 检查两个实例的hash值
		* Instance 1 hash:5433634
		* Instance 2 hash:2430287
		* 
		* 根据哈希值可以看出，反射破坏了单例的特性，因此懒汉式StatisticInnerClassLazySingleton版诞生了
		* @on
		*/
		System.out.println("Instance 1 hash:" + instance1.hashCode());
		System.out.println("Instance 2 hash:" + instance2.hashCode());
	}
}
