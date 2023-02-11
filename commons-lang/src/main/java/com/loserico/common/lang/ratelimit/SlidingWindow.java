package com.loserico.common.lang.ratelimit;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 滑动时间窗口
 * <p>
 * Copyright: (C), 2022-11-18 14:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SlidingWindow implements RateLimiter {
	
	/**
	 * 访问计数器
	 */
	private final AtomicLong counter = new AtomicLong();
	
	/**
	 * 存储每个子窗口的计数
	 */
	private LinkedList<Long> slots = new LinkedList<>();
	
	/**
	 * 时间窗口, 即对多长时间内的访问进行限流, 最终都会转换成毫秒进行时间窗口的切割
	 */
	private Long timeWindow;
	
	/**
	 * 时间窗口的单位, 比如可以指定1分钟, 1小时等
	 */
	private TimeUnit timeUnit;
	
	/**
	 * 精度, 即要将timeWindow划分成多少个子窗口, 划得越多精度越高
	 */
	private int precision = 10;
	
	/**
	 * 在指定的时间窗口内允许通过多少个请求
	 */
	private Long limit;
	
	/**
	 * 设置了时间窗口timeWindow和要划分多少个子窗口precision后, 计算得出的每个子窗口的毫秒数
	 */
	private final long subWindowMillis;
	
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	
	public SlidingWindow(Long timeWindow, TimeUnit timeUnit, Integer precision, Long limit) {
		Objects.requireNonNull(timeWindow, "timeWindows cannot be null!");
		Objects.requireNonNull(timeUnit, "timeUnit cannot be null!");
		Objects.requireNonNull(precision, "precision cannot be null!");
		Objects.requireNonNull(limit, "limit cannot be null!");
		this.timeWindow = timeWindow;
		this.timeUnit = timeUnit;
		this.precision = precision;
		this.limit = limit;
		long timeWindowInMillis = timeUnit.toMillis(timeWindow);
		subWindowMillis = timeWindowInMillis / precision;
		scheduledExecutorService.scheduleAtFixedRate(() -> {
			slots.add(counter.get());
			//将子窗口限制在指定的数量内
			if (slots.size() > precision) {
				slots.removeFirst();
			}
			if (log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				for (Long slot : slots) {
					sb.append(slot).append("-");
				}
				sb.deleteCharAt(sb.length()-1);
				if (log.isDebugEnabled()) {
					log.debug("slots现在的结构: {}", sb.toString());
					log.debug("counter现在的计数: {}", counter.get());
				}
			}
		}, 0, subWindowMillis, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public boolean canPass() {
		boolean canPass = false;
		if (slots.size() == 0 || slots.size() == 1) {
			canPass = counter.get() < limit;
		} else {
			/*
			 * 注意这里要用counter.get(), 不能用slots.peekLast(), 因为定时器有可能还没有放计数放入最后一个slot里面
			 */
			canPass = (counter.get() - slots.peekFirst() < limit);
		}
		
		if (canPass) {
			counter.incrementAndGet();
		}
		
		return canPass;
	}
}
