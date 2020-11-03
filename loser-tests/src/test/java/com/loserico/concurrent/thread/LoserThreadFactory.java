package com.loserico.concurrent.thread;

import java.util.concurrent.ThreadFactory;

/**
 * <p>
 * Copyright: (C), 2020-09-30 14:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserThreadFactory implements ThreadFactory {
	
	private int threadId;
	private String name;
	
	public LoserThreadFactory(String name) {
		this.threadId = 1;
		this.name = name;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + "-Thread_" + threadId);
		System.out.println("created new thread with id : " + threadId + " and name : " + t.getName());
		this.threadId++;
		return t;
	}
}
