package com.loserico.concurrent.threadpool;

public interface RejectedExecutionHandler {
	
	void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
