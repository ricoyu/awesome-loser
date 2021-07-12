package com.loserico.common.lang.concurrent;

/**
 * 线程池拒绝策略 
 * <p>
 * Copyright: Copyright (c) 2021-07-10 12:36
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Policy {
	CALLER_RUNS,
	
	ABORT,
	
	ABORT_WITH_REPORT,
	
	DISCARD,
	
	DISCARD_OLDEST;
}
