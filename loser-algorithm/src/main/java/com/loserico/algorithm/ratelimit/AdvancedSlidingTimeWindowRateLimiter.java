package com.loserico.algorithm.ratelimit;

import java.util.LinkedList;

/**
 * 使用子窗口的滑动时间窗口限流器。 
 * <p>
 * Copyright: Copyright (c) 2023-10-27 9:51
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AdvancedSlidingTimeWindowRateLimiter {

    // 整个时间窗口的大小（以毫秒为单位）
    private final long windowSizeInMillis;
    // 子窗口的总数
    private final int totalSubWindows;
    // 每个子窗口允许的最大请求数
    private final int[] maxRequestsPerSubWindow;
    // 用于记录每个子窗口的请求时间戳
    private final LinkedList<Long>[] subWindows;
    // 当前活跃的子窗口索引
    private int currentSubWindowIndex = 0;

    /**
     * 构造函数
     * 
     * @param windowSizeInMillis       整个时间窗口的大小（以毫秒为单位）
     * @param maxRequestsPerSubWindow  每个子窗口允许的最大请求数
     */
    @SuppressWarnings("unchecked")
    public AdvancedSlidingTimeWindowRateLimiter(long windowSizeInMillis, int[] maxRequestsPerSubWindow) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.totalSubWindows = maxRequestsPerSubWindow.length;
        this.maxRequestsPerSubWindow = maxRequestsPerSubWindow;
        // 初始化每个子窗口的时间戳列表
        this.subWindows = (LinkedList<Long>[]) new LinkedList[totalSubWindows];
        for (int i = 0; i < totalSubWindows; i++) {
            subWindows[i] = new LinkedList<>();
        }
    }

    /**
     * 尝试获取请求权限
     * 
     * @return 如果在当前子窗口内允许请求，则返回true，否则返回false。
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // 计算当前时间对应的子窗口索引
        long elapsedMillis = now % windowSizeInMillis;
        int targetSubWindow = (int) ((elapsedMillis * totalSubWindows) / windowSizeInMillis);

        // 如果目标子窗口与当前子窗口不同，清空目标子窗口的请求时间戳
        if (targetSubWindow != currentSubWindowIndex) {
            subWindows[targetSubWindow].clear();
            currentSubWindowIndex = targetSubWindow;
        }

        // 清除当前子窗口内的过期请求时间戳
        while (!subWindows[currentSubWindowIndex].isEmpty()
                && (now - subWindows[currentSubWindowIndex].getFirst() > windowSizeInMillis / totalSubWindows)) {
            subWindows[currentSubWindowIndex].removeFirst();
        }

        // 检查当前子窗口内的请求是否超过限制
        if (subWindows[currentSubWindowIndex].size() < maxRequestsPerSubWindow[currentSubWindowIndex]) {
            subWindows[currentSubWindowIndex].addLast(now);
            return true;
        }

        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        // 每秒钟允许5个请求，其中前半秒最多3个请求，后半秒最多2个请求
        AdvancedSlidingTimeWindowRateLimiter limiter = new AdvancedSlidingTimeWindowRateLimiter(1000, new int[]{3, 2});

        // 模拟请求
        for (int i = 0; i < 10; i++) {
            System.out.println("Request " + (i + 1) + ": " + (limiter.tryAcquire() ? "Allowed" : "Denied"));
            Thread.sleep(100);
        }
    }
}
