package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

public class MergeSort {
    
    @Test
    public void test() {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        mergeSort(arr);
        AlgorithmUtils.printArray(arr);
    }

    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length-1);
    }

    public static void process(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int m = l + ((r - l) >> 1);
        process(arr, l, m);
        process(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    public static void merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int p1 = l;
        int p2 = m + 1;
        int i = 0;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }

        /*
         * 接下来可能左边半个数组还没复制完, 也有可能右边半个数组还没复制完, 把剩下的再拷贝到help数组里面
         * 但实际下面两个while只会执行一个
         */
        while (p1<=m) {
            help[i++] = arr[p1++];
        }
        while (p2<=r) {
            help[i++] = arr[p2++];
        }

        for (int j = 0; j < help.length; j++) {
            arr[l+j] = help[j];
        }
    }
}
