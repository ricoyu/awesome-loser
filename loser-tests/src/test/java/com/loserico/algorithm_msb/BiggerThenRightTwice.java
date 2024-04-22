package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * num右边乘2都不如num大的数个数
 * 一个数组, 找出这个数组中某个位置的num, 他右边有多少个数乘以2后都不如num大, 最后求总个数
 * <p>
 * Copyright: Copyright (c) 2024-04-15 9:58
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BiggerThenRightTwice {

    public static void main(String[] args) {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        int count = process(arr, 0, arr.length - 1);
        AlgorithmUtils.printArray(arr);
        System.out.println(count);
    }

    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }

        int m = l + ((r - l) >> 1);
        return process(arr, 0, m) + process(arr, m + 1, r) + merge(arr, l, m, r);
    }

    public static int merge(int[] arr, int l, int m, int r) {
        int indexR = m + 1;
        int ans = 0;
        for (int i = l; i <= m; i++) {
            while (indexR <= r && arr[i] > arr[indexR] * 2) {
                indexR++;
            }
            ans += indexR - m - 1;
        }

        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }

        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }

        for (int j = 0; j < help.length; j++) {
            arr[l + j] = help[j];
        }

        return ans;
    }
}
