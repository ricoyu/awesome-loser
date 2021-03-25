package com.loserico.ratelimiter;

/**
 * 令牌桶
 * <p>
 * Copyright: (C), 2021-02-20 18:04
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TokenBucketDemo {
	
	private static long time = System.currentTimeMillis();
	
	private static int createTokenRate = 3;
	
	private static int size = 10;
	
	/**
	 * 当前令牌数
	 */
	private static int tokens = 0;
	
	public static boolean grant() {
		long now = System.currentTimeMillis();
		//在这段时间内需要产生的令牌数
		int in = (int) ((now - time) / 50 * createTokenRate);
		tokens = Math.min(size, tokens + in);
		time = now;
		
		if (tokens > 0) {
			--tokens;
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 500; i++) {
			new Thread(() -> {
				if (grant()) {
					System.out.println("执行业务逻辑");
				} else {
					System.out.println("限流");
				}
			}).start();
		}
	}
}
