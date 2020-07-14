package com.loserico.concurrent.lock;

/**
 * 锁粗化
 * <p>
 * Copyright: (C), 2019/11/18 11:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LockCoarsing {
	
	StringBuffer sb = new StringBuffer();
	
	public void test() {
		/*
		 * StringBuffer是线程安全的
		 * public synchronized StringBuffer append(String str)
		 * 下面调用4次append相当于4次进出synchronized块, 我们知道重量级锁或涉及上下文切换, 
		 * 因为用到了操作系统的互斥量, 会涉及用户态到内核态的切换, 这样的话效率是很低的
		 * 但是现代JVM会对下面这段代码做优化, 会生成一个大的synchronized块包住四个append
		 * 这就是"锁粗化"
		 */
		sb.append("1");
		sb.append("2");
		sb.append("3");
		sb.append("4");
	}
	
	public static void main(String[] args) {
		LockCoarsing lockCoarsing = new LockCoarsing();
	}
}
