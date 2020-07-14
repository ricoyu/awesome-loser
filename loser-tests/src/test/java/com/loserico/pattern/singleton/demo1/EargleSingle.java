package com.loserico.pattern.singleton.demo1;

/**
 * 它基于 classloader 机制避免了多线程的同步问题，不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，
 * 在单例模式中大多数都是调用 getInstance 方法，但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，
 * 这时候初始化 instance 显然没有达到 lazy loading 的效果。
 * <p>
 * Copyright: (C), 2020/6/30 15:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EargleSingle {
	
	private static EargleSingle instance = new EargleSingle();
	
	private EargleSingle() {
	}
	
	public static EargleSingle getInstance() {
		return instance;
	}
}
