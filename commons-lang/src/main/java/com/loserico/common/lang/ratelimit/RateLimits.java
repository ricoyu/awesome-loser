package com.loserico.common.lang.ratelimit;

import java.util.concurrent.TimeUnit;

/**
 * 限流组件
 * <p>
 * Copyright: (C), 2022-11-18 15:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RateLimits {
	
	/**
	 * 滑动时间窗口限流
	 * @param timeWindow 在多长时间内限流多少请求, 后面还会用precision将时间窗口划分成多个小窗口
	 * @param timeUnit timeWindow的单位
	 * @return SlidingWindowRateLimiterBuilder
	 */
	public static SlidingWindowRateLimiterBuilder slidingTimeWindow(Long timeWindow, TimeUnit timeUnit) {
		return new SlidingWindowRateLimiterBuilder(timeWindow, timeUnit);
	}
}
