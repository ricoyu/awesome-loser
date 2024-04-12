package com.loserico.pattern.behavioral.strategy;

/**
 * 客户端代码
 * <p>
 * Copyright: Copyright (c) 2024-04-02 10:23
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StrategyPatternDemo {

    public static void main(String[] args) {
        int[] numbers = {1, 3, 2, 5, 4};

        Sorter sorter = new Sorter(new QuickSortStrategy());
        sorter.sort(numbers);

        sorter.setStrategy(new BubbleSortStrategy());
        sorter.sort(numbers);
    }
}
