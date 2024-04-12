package com.loserico.pattern.behavioral.strategy;

/**
 * 快速排序策略
 * <p>
 * Copyright: Copyright (c) 2024-04-02 10:22
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class QuickSortStrategy implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        // 实现快速排序逻辑
        System.out.println("Sorting array using quick sort");
    }
}