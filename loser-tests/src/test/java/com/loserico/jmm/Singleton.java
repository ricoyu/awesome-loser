package com.loserico.jmm;

/**
 * <p>
 * Copyright: (C), 2019/11/17 8:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Singleton {
	
	/**
	 * 查看汇编指令
	 * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
	 */
	private static volatile Singleton instance;
	
	private Singleton() {
	}
	
	public static Singleton getInstance() {
		if (instance == null) {
			synchronized (Singleton.class) {
				if (instance == null) {
					/**
					 * 对象创建过程本质上可以分为三步
					 * 1: 申请内存空间, 拿到一个内存地址
					 * 2: 实例化对象
					 * 3: 将内存地址赋值给instance
					 * 这三步连在一起是无法保证原子性的
					 *
					 * 并且这三步之间也有可能发生指令重排, 所以加volatile关键字的另一个目的是防止指令重排
					 * (比如2, 3两步就可能发生指令重排)
					 */
					instance = new Singleton();
				}
			}
		}
		
		return instance;
	}
	
	public static void main(String[] args) {
		Singleton.getInstance();
	}
}
