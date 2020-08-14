package com.loserico.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://juejin.im/post/5aaa2ba8f265da239530b69e
 * 
 * Copy-On-Write，顾名思义，在计算机中就是当你想要对一块内存进行修改时，我们不在原有内存块中进行写操作，而是将内存拷贝一份，
 * 在新的内存中进行写操作，写完之后呢，就将指向原来内存指针指向新的内存，原来的内存就可以被回收掉嘛！
 * <p>
 * Copyright: (C), 2020-7-23 0023 16:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CopyOnWriteArrayListTest {
	
	private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		
		list.add("1");
		list.add("2");
		list.add("3");
		
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
			service.execute(new Runnable() {
				@Override
				public void run() {
					list.add("121");// 添加数据
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
			service.execute(new Runnable() {
				@Override
				public void run() {
					while (iter.hasNext()) {
						System.err.println(iter.next());
					}
				}
			});
		}
		
		System.err.println(Arrays.toString(list.toArray()));
		
	}
}
