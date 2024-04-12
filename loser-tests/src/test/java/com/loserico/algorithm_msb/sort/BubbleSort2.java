package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

/**
 * 这是用来反复练习的
 * <p>
 * Copyright: Copyright (c) 2024-04-02 10:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BubbleSort2 {

    @Test
    public void test() {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        bubblesort(arr);
        AlgorithmUtils.printArray(arr);
    }
    public static void bubblesort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i -1; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr, j, j+1);
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }
}
