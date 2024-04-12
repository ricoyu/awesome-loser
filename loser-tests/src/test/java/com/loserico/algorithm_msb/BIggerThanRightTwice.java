package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * num右边乘2都不如num大的数
 * 一个数组, 找出这个数组中某个位置的num, 他右边有多少个数乘以2后都不如num大, 最后求总个数
 * <p>
 * Copyright: Copyright (c) 2024-04-11 10:20
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BIggerThanRightTwice {

    public static void main(String[] args) {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        int ans = process(arr, 0, arr.length - 1);
        AlgorithmUtils.printArray(arr);
        System.out.println(ans);
    }

    public static int process(int[] arr, int l , int r) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        if (l == r) {
            return 0;
        }

        int mid = l + ((r-l) >>1);
        return process(arr, l, mid) + process(arr, mid+1, r) + merge(arr, l , mid, r);
    }

    private static int merge(int[] arr, int l, int mid, int r) {
        int p1 = l;
        int p2 = mid+1;
        int ans = 0;
        int p3 = mid+1;

        for (int i = l; i <=mid ; i++) {
            while (p3 <=r && arr[i] > arr[p3] * 2) {
                p3++;
            }
            ans+=p3-mid-1;
        }

        int[] help = new int[r-l+1];
        int i = 0;
        p1 = l;
        p2= mid+1;
        while (p1<=mid && p2<=r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }

        while (p1<=mid) {
            help[i++] = arr[p1++];
        }
        while (p2<=r) {
            help[i++] = arr[p2++];
        }

        for (int j = 0; j < help.length; j++) {
            arr[l+j] = help[j];
        }

        return ans;
    }
}
