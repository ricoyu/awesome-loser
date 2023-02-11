package com.loserico.common.lang.ratelimit;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2022-11-18 15:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class SlidingWindowRateLimiterBuilder {
	
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
	 * 时间窗口, 即对多长时间内的访问进行限流, 最终都会转换成毫秒进行时间窗口的切割
	 * @param timeWindow  时间长度
	 * @param timeUnit    时间单位
	 * @return SlidingWindowRateLimiterBuilder
	 */
	SlidingWindowRateLimiterBuilder(Long timeWindow, TimeUnit timeUnit) {
		this.timeWindow = timeWindow;
		this.timeUnit = timeUnit;
	}
	
	/**
	 * 精度, 即要将timeWindow划分成多少个子窗口, 划得越多精度越高
	 * @param precision
	 * @return SlidingWindowRateLimiterBuilder
	 */
	public SlidingWindowRateLimiterBuilder precision(Integer precision) {
		Objects.requireNonNull(precision, "precision cannot be null!");
		return this;
	}
	
	/**
	 * 在指定的时间窗口内允许通过多少个请求
	 * @param limit
	 * @return SlidingWindowRateLimiterBuilder
	 */
	public SlidingWindowRateLimiterBuilder limit(Long limit) {
		this.limit = limit;
		return this;
	}
	
	public SlidingWindow build() {
		return new SlidingWindow(timeWindow, timeUnit, precision, limit);
	}
}
