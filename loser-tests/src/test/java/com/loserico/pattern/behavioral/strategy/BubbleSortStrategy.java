package com.loserico.pattern.behavioral.strategy;

/**
 * 冒泡排序策略
 * <p>
 * Copyright: Copyright (c) 2024-04-02 10:23
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BubbleSortStrategy implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        // 实现冒泡排序逻辑
        System.out.println("Sorting array using bubble sort");
    }
}