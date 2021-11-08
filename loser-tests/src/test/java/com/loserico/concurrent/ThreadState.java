package com.loserico.concurrent;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * Copyright: (C), 2021-11-06 11:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ThreadState {
	
	@SneakyThrows
	@Test
	public void testState() {
		Thread t = new Thread();
		System.out.println("线程状态 " + t.getState()); //线程状态 NEW
		t.start();
		System.out.println("线程状态 " + t.getState()); //线程状态 RUNNABLE
		Thread.sleep(100);
		System.out.println("线程状态 " + t.getState()); //线程状态 TERMINATED
	}
	
	@SneakyThrows
	@Test
	public void testState2() {
		Thread t = new Thread(() -> {
			LockSupport.park();
		});
		System.out.println("线程状态 " + t.getState()); //线程状态 NEW
		t.start();
		System.out.println("线程状态 " + t.getState()); //线程状态 RUNNABLE
		Thread.sleep(100);
		System.out.println("线程状态 " + t.getState()); //线程状态 WAITING
	}
}
