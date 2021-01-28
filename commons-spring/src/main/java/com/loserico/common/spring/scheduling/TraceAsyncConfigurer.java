package com.loserico.common.spring.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 把traceId传递到子线程的异步执行器
 * <p>
 * Copyright: (C), 2020/1/2 20:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
@Component
public class TraceAsyncConfigurer implements AsyncConfigurer {
	
	@Value("${spring.async.max-pool-size:500}")
	private int maxPoolSize;
	
	@Value("${spring.async.thread-name-prefix:async-trace-pool-}")
	private String threadNamePrefix;
	
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int cores = Runtime.getRuntime().availableProcessors();
		executor.setCorePoolSize(cores);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.setTaskDecorator(new MDCTaskDecorator());
		/*
		 * 设置线程池关闭的时候等待所有任务完成
		 */
		executor.setWaitForTasksToCompleteOnShutdown(true);
		/*
		 * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
		 */
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, params) -> log.error("asyc execute error, method={}, params={}", method.getName(), Arrays.toString(params));
	}
	
	public static class MDCTaskDecorator implements TaskDecorator {
		
		@Override
		public Runnable decorate(Runnable runnable) {
			Map<String, String> contextMap = MDC.getCopyOfContextMap();
			return () -> {
				if (contextMap != null) {
					MDC.setContextMap(contextMap);
				}
				try {
					runnable.run();
				} finally {
					MDC.clear();
				}
			};
		}
	}
}
