package com.loserico.algorithm.ratelimit;

import java.util.LinkedList;

/**
 * 滑动时间窗口限流器 
 * <p>
 * Copyright: Copyright (c) 2023-10-27 9:40
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SlidingTimeWindowRateLimiter {

    /**
     * 窗口大小（以毫秒为单位）
     */
    private final long windowSizeInMillis;

    /**
     * 窗口内的最大请求数
     */
    private final int maxRequests;

    /**
     * 记录请求时间的队列
     */
    private final LinkedList<Long> timestamps;

    public SlidingTimeWindowRateLimiter(long windowSizeInMillis, int maxRequests) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.maxRequests = maxRequests;
        this.timestamps = new LinkedList<>();
    }

    /**
     * 尝试获取请求的权限。
     * 
     * @return 如果在当前滑动窗口内允许请求，则返回true，否则返回false。
     */
    public synchronized boolean tryAcquire() {
        // 获取当前时间
        long now = System.currentTimeMillis();
        
        // 清除过时的时间戳
        while (!timestamps.isEmpty() && (now - timestamps.getFirst() > windowSizeInMillis)) {
            timestamps.removeFirst();
        }

        // 检查当前滑动窗口内的请求数是否超过限制
        if (timestamps.size() < maxRequests) {
            timestamps.addLast(now);
            return true;
        }
        
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindowRateLimiter limiter = new SlidingTimeWindowRateLimiter(1000, 5);

        // 模拟请求
        for (int i = 0; i < 10; i++) {
            System.out.println("Request " + (i + 1) + ": " + (limiter.tryAcquire() ? "Allowed" : "Denied"));
            Thread.sleep(200);
        }
    }
}
