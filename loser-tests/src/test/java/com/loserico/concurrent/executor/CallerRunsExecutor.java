package com.loserico.concurrent.executor;

import java.util.concurrent.Executor;

/**
 * 直接在调用者相同的线程中执行
 * <p>
 * Copyright: (C), 2021-01-28 13:45
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CallerRunsExecutor implements Executor {
	
	@Override
	public void execute(Runnable task) {
		task.run();
	}
}
