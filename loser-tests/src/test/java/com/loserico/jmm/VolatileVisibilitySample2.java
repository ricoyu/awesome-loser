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
public class VolatileVisibilitySample2 {
	
	private boolean initFlag = false;
	
	static Object object = new Object();
	
	public void refresh() {
		/**
		 * 这是一个普通写操作, 写完之后等待某个时间点写回到主内存
		 */
		this.initFlag = true;
		String threadName = Thread.currentThread().getName();
		log.info("线程：" + threadName + ":修改共享变量initFlag");
	}
	
	public void load() {
		String threadName = Thread.currentThread().getName();
		int i = 0;
		
		/**
		 * 加上synchronized (object)就可以感知到initFlag的改变
		 * 因为在同步块内跑可能会引起线程上下文的切换, 因为要竞争共享资源object
		 * 而线程在重新获得CPU资源, 加载线程执行上下文的时候就有可能会去
		 * 主内存里面重新读取变量的值, 所以这边能够感知到到initFlag的改变
		 */
		while (!initFlag) {
			synchronized (object) {
				i++;
			}
		}
		log.info("线程：" + threadName + "当前线程嗅探到initFlag的状态的改变" + i);
	}
	
	public static void main(String[] args) {
		VolatileVisibilitySample2 sample = new VolatileVisibilitySample2();
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
