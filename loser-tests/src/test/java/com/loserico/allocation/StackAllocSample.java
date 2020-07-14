package com.loserico.allocation;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2019/11/18 9:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StackAllocSample {
	
	/**
	 * 进行两种测试
	 * 1: 关闭逃逸分析, 同时调大堆空间, 避免堆内GC的发生, 如果发生了GC, 打印GC log
	 * VM运行参数: -Xms4G -Xmx4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=stack-alloc-sample.pref -Xloggc:gc.log
	 * <p>
	 * 2: 开启逃逸分析
	 * VM运行参数: -Xms4G -Xmx4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=stack-alloc-sample.pref -Xloggc:gc.log
	 * 
	 * 执行main方法后
	 * jps 查看进程
	 * jmap -histo 进程ID
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 500000; i++) {
			alloc();
		}
		long end = System.currentTimeMillis();
		//查看执行时间
		System.out.println("cost-time " + (end - start) + " ms");
		try {
			TimeUnit.MILLISECONDS.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static Student alloc() {
		/*
		 * JIT在编译时会对代码进行逃逸分析
		 * 并不是所有对象都存放在堆区, 有的存在线程栈空间(实测开启逃逸分析后, 有15万个左右在堆上创建)
		 */
		Student student = new Student();
		return student;
	}
	
	static class Student {
		private String name;
		private int age;
	}
}
