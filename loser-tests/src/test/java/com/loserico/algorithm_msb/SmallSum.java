package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

/**
 * 小和问题
 * 求一个数组的小和
 * <p>
 * 小和的定义如下：
 * <p>
 * 例如，数组s=[1,3,5,2,4,6]，在s[0]的左边小于或等于s[0]的数的和为0，在s[1]的左边小于或等于s[1]的数的和为1，在s[2]的左边小于或等于s[2]的数的和为1+3=4，
 * 在s[3]的左边小于或等于s[3]的数的和为1，在s[4]的左边小于或等于s[4]的数的和为1+3+2=6，在s[5]的左边小于或等于s[5]的数的和为1+3+5+2+4=15，
 * 所以s的小和为0+1+4+1+6+15=27。
 * <p>
 * 给定一个数组s，实现函数返回s的小和
 * <p>
 * 要求：时间复杂度O(NlogN)，额外空间复杂度O(N)
 * <p>
 * 思路：
 * <p>
 * 1. 采用归并排序的思想
 * 2. 在merge的过程中，如果左边数组的元素小于右边数组的元素，那么左边数组的元素就会产生小和
 * 3. 举例说明：左边数组[1,3,5]，右边数组[2,4,6]，左边数组的元素5大于右边数组的元素2，那么5就会产生小和
 * 4. 代码实现：在merge的过程中，如果左边数组的元素小于右边数组的元素，那么左边数组的元素就会产生小和，同时左边数组
 * 的元素和右边数组的元素都是有序的，所以左边数组的元素小于右边数组的元素，那么左边数组的元素就会产生小和，同时左边数组
 * 的元素也是有序的，所以左边数组的元素产生小和的个数就是右边数组剩余元素的个数
 * <p>
 * <p>
 * Copyright: Copyright (c) 2024-04-10 10:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SmallSum {

    @Test
    public void testSmallSum() {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        int smallSum = process(arr, 0, arr.length - 1);
        System.out.println(smallSum);
    }

    public static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }

        int mid = l + ((r - l) >> 1);
        return process(arr, l, mid) + process(arr, mid + 1, r) + merge(arr, l, mid, r);
    }

    public static int merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = r;
        int ans = 0;
        while (p1 <= m && p2 <= r) {
            ans += arr[p1] < arr[p2] * (r - p2 + 1) ? arr[p1] : 0;
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
