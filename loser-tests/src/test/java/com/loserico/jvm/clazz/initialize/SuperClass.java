package com.loserico.jvm.clazz.initialize;

/**
 * 被动使用类字段演示一:
 * 通过子类引用父类的静态字段, 不会导致子类初始化
 * <p>
 * Copyright: Copyright (c) 2019-08-25 16:51
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class SuperClass {

	static {
		System.out.println("Super class init!");
	}

	public static int value = 123;
}
