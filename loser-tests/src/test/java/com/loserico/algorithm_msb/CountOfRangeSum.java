package com.loserico.algorithm_msb;

/**
 * 返回arr有多少个子数组, 给定给一个数组arr, 两个整数lower和upper,
 * 返回arr中有多少个子数组的累加和在[lower, upper]范围上
 * <p>
 * 这道题直接在leetcode测评：
 * https://leetcode.com/problems/count-of-range-sum/
 * <p>
 * Copyright: Copyright (c) 2024-04-12 10:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CountOfRangeSum {

    public static int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null && nums.length == 0) {
            return 0;
        }

        long[] sums = new long[nums.length];
        sums[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            sums[i] = sums[i - 1] + nums[i];
        }

        return process(sums, 0, sums.length - 1, lower, upper);
    }

    public static int process(long[] sums, int L, int R, int lower, int upper) {
        if (L == R) {
            return sums[L] >= lower && sums[R] <= upper ? 1 : 0;
        }

        int mid = L + ((R - L) >> 1);
        return process(sums, L, mid, lower, upper) + process(sums, mid + 1, R, lower, upper) + merge(sums, L, mid, R, lower, upper);
    }

    public static int merge(long[] sums, int L, int M, int R, int lower, int upper) {
        int ans = 0;
        int windowL = L;
        int windowR = L;
        //[windowL, windowR)
        for (int i = M + 1; i <= R; i++) {
            long min = sums[i] - upper;
            long max = sums[i] - lower;

            while (windowR <= M && sums[windowR] <= max) {
                windowR++;
            }
            while (windowL <= M && sums[windowL] < min) {
                windowL++;
            }
            ans += windowR - windowL;
        }

        long[] help = new long[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = M + 1;
        while (p1 <= M && p2 <= R) {
            help[i++] = sums[p1] < sums[p2] ? sums[p1++] : sums[p2++];
        }

        while (p1 <= M) {
            help[i++] = sums[p1++];
        }
        while (p2 <= R) {
            help[i++] = sums[p2++];
        }
        for (int j = 0; j < help.length; j++) {
            sums[L + j] = help[j];
        }
        return ans;
    }
}
