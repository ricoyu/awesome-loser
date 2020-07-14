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
public class VolatileVisibilitySample {
	
	private boolean initFlag = false;
	
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
		/**
		 * threadB要感知到threadA回写的initFlag, 必须通过CPU的总线嗅探机制来实现
		 * 而总线嗅探是需要Java指令生成汇编指令的时候加上一个#lock指令才会去嗅探
		 * 这边是一个普通变量, 所以生成的汇编指令中没有#lock指令, 即CPU不会去做总线嗅探
		 * 所以threadA感知不到threadB已经修改了initflag
		 * 
		 * 就是说这里没有使用到MESI缓存一致性协议
		 */
		while (!initFlag) {
			//while空循环
		}
		log.info("线程：" + threadName + "当前线程嗅探到initFlag的状态的改变");
	}
	
	/**
	 * 这个版本threadB永远不会嗅探到initFlag的改变
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		VolatileVisibilitySample sample = new VolatileVisibilitySample();
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
