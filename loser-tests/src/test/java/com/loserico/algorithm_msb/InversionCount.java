package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * 产生逆序对
 * 一个数组中, 左边的数比右边大成为逆序对, 求这个数组中有多少个逆序对?
 * <p>
 * Copyright: Copyright (c) 2024-04-10 20:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class InversionCount {

    public static void main(String[] args) {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        System.out.println("Total inversions: " + countInversions(arr));
    }

    //主函数，用于调用归并排序的辅助函数并返回逆序对的总数
    public static int countInversions(int[] arr) {
        int[] temp = new int[arr.length];
        return mergeSort(arr, temp, 0, arr.length - 1);
    }

    // 归并排序函数，同时计算逆序对
    private static int mergeSort(int[] arr, int[] temp, int l, int r) {
        int mid, invCount = 0;
        if (r > l) {
            mid = l + ((r - l) >> 1);
            //计算左半部分的逆序对
            invCount += mergeSort(arr, temp, l, mid);
            // 计算右半部分的逆序对
            invCount += mergeSort(arr, temp, mid + 1, r);
            // 合并两个部分，并计算跨越两部分的逆序对
            invCount += merge(arr, temp, l, mid + 1, r);
        }

        return invCount;
    }

    private static int merge(int[] arr, int[] temp, int l, int mid, int r) {
        int p1, p2, k;
        int invCount = 0;

        p1 = l; //左半部分的起始索引
        p2 = mid; //右半部分的起始索引
        k = l; //合并后数组的起始索引
        while ((p1 <= mid - 1) && (p2 <= r)) {
            if (arr[p1] <= arr[p2]) {
                temp[k++] = arr[p1++];
            } else {
                temp[k++] = arr[p2++];
                invCount = invCount + (mid - p1); // 核心：计算逆序对的个数
            }
        }

        while (p1<=mid-1) {
            temp[k++] = arr[p1++];
        }
        while(p2<=r) {
            temp[k++] = arr[p2++];
        }

        return invCount;
    }
}
