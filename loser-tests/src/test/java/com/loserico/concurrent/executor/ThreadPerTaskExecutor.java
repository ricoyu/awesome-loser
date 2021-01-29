package com.loserico.concurrent.executor;

import java.util.concurrent.Executor;

/**
 * 每来一个task就新起一个线程执行
 * <p>
 * Copyright: (C), 2021-01-28 13:47
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ThreadPerTaskExecutor implements Executor {
	
	@Override
	public void execute(Runnable task) {
		new Thread(task).start();
	}
}
