package com.loserico.concurrent.executor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * <p>
 * Copyright: (C), 2021-01-28 13:50
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SerialExecutor implements Executor {
	
	private final Queue<Runnable> tasks = new ArrayDeque<>();
	
	private final Executor executor;
	
	private Runnable active;
	
	public SerialExecutor(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void execute(Runnable task) {
		tasks.offer(new Runnable() {
			@Override
			public void run() {
				try {
					task.run();
				} finally {
					scheduleNext();
				}
			}
		});
		
		if (active == null) {
			scheduleNext();
		}
	}
	
	protected synchronized void scheduleNext() {
		if ((active = tasks.poll()) != null) {
			executor.execute(active);
		}
	}
}
