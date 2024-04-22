package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * 演示最简单的递归
 * <p>
 * Copyright: Copyright (c) 2024-04-12 21:55
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxElem {

    public static void main(String[] args) {
        int[] arr = AlgorithmUtils.randomArr(6, 9);
        AlgorithmUtils.printArray(arr);
        int max = process(arr, 0, arr.length - 1);
        System.out.println(max);
    }

    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l];
        }

        int mid = l + ((r - l) >> 1);
        int left = process(arr, l, mid);
        int right = process(arr, mid + 1, r);
        return Math.max(left, right);
    }
}
