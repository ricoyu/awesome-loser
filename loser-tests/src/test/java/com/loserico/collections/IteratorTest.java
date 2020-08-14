package com.loserico.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程的环境会有问题的代码, 因为多线程环境中，你在迭代的时候是不允许有其他线程对这个集合list进行添加元素的
 * <p>
 * Copyright: (C), 2020-7-23 0023 16:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IteratorTest {
	
	private static List<String> list = new ArrayList<>();
	
	public static void main(String[] args) {
		list.add("1");
		list.add("2");
		list.add("3");
		
		//singleThreadOK();
		
		concurrentThrowException();
	}
	
	/**
	 * 多线程会对迭代集合产生影响，影响读操作
	 */
	private static void concurrentThrowException() {
		Iterator<String> iter = list.iterator();
		
		// 存放10个线程的线程池
		ExecutorService service = Executors.newFixedThreadPool(10);
		
		// 执行10个任务(我当前正在迭代集合（这里模拟并发中读取某一list的场景）)
		for (int i = 0; i < 10; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					while (iter.hasNext()) {
						System.err.println(iter.next());
					}
				}
			});
		}
		
		// 执行10个任务
		for (int i = 0; i < 10; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					list.add("121");// 添加数据
				}
			});
		}
		
		System.err.println(Arrays.toString(list.toArray()));
	}
	
	private static void singleThreadOK() {
		Iterator<String> it = list.iterator();
		//我当前正在迭代集合（这里模拟并发中读取某一list的场景）
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
		System.err.println(Arrays.toString(list.toArray()));
	}
}
