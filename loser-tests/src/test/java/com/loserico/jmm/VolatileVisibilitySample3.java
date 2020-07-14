package com.loserico.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Java 内存模型之内存操作
 * <p>
 * lock(锁定)：  作用于主内存的变量，把一个变量标记为一条线程独占状态<p>
 * unlock(解锁)：作用于主内存的变量，把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定<p>
 * read(读取)：  作用于主内存的变量，把一个变量值从主内存传输到线程的工作内存中，以便随后的load动作使用<p>
 * load(载入)：  作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中<p>
 * use(使用)：   作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎<p>
 * assign(赋值)：作用于工作内存的变量，它把一个从执行引擎接收到的值赋给工作内存的变量<p>
 * store(存储)： 作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，以便随后的write的操作<p>
 * write(写入)： 作用于工作内存的变量，它把store操作从工作内存中的一个变量的值传送到主内存的变量中<p>
 * <p>
 * Copyright: (C), 2019/11/13 10:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class VolatileVisibilitySample3 {
	
	private volatile boolean initFlag = false;
	
	public void refresh() {
		this.initFlag = true;
		String threadName = Thread.currentThread().getName();
		log.info("线程：" + threadName + ":修改共享变量initFlag");
	}
	
	public void load() {
		String threadName = Thread.currentThread().getName();
		
		/**
		 * initFlag加了volatile关键字修饰后threadB就能感知到threadA对initFlag的修改
		 * 
		 * 加了volatile关键字后, 生成的汇编指令就有一个lock前缀
		 * MESI缓存一致性协议就在这边生效了
		 */
		while (!initFlag) {
		}
		log.info("线程：" + threadName + "当前线程嗅探到initFlag的状态的改变");
	}

	public static void main(String[] args) {
		VolatileVisibilitySample3 sample = new VolatileVisibilitySample3();
		Thread threadA = new Thread(() -> {
			sample.refresh();
		}, "threadA");
		
		Thread threadB = new Thread(() -> {
			sample.load();
		}, "threadB");
		
		threadB.start();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadA.start();
	}
}
