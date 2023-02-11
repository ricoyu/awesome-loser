package com.loserico.common.lang.ratelimit;

/**
 * <p>
 * Copyright: (C), 2022-11-18 15:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface RateLimiter {
	
	/**
	 * 判断是否放行, 如果放行, 计数器+1
	 * @return
	 */
	public boolean canPass();
}
