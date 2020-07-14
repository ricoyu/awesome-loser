package com.loserico.pattern.singleton.demo1;

/**
 * 这种实现方式还没有被广泛采用, 但这是实现单例模式的最佳方法。它更简洁, 自动支持序列化机制, 绝对防止多次实例化。
 * 
 * 种方式是 Effective Java 作者 Josh Bloch 提倡的方式, 它不仅能避免多线程同步问题, 而且还自动支持序列化机制, 防止反序列化重新创建新的对象, 绝对防止多次实例化。
 * 不过, 由于 JDK1.5 之后才加入 enum 特性, 用这种方式写不免让人感觉生疏, 在实际工作中, 也很少用。
 * 
 * 不能通过 reflection attack 来调用私有构造方法。
 * 
 * <p>
 * Copyright: Copyright (c) 2020-06-30 16:09
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum EnumSingleton {
	
	INSTANCE;
	
	public void whateverMethod() {
		System.out.println("这是enum实现的单例");
	}
}
