package com.loserico.unsafe;

import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * Copyright: (C), 2019/11/22 13:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadParkerTest2 {
	
	public static void main(String[] args) {
		//相当于先往池子里放了一张票据
		LockSupport.unpark(Thread.currentThread());
		System.out.println("I'm running step1");
		
		//拿出票据使用
		LockSupport.park();
		System.out.println("main thread is over");
	}
}
