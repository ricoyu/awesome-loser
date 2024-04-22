package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

import static org.junit.Assert.assertTrue;

/**
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *
 * 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 * <p>
 * Copyright: Copyright (c) 2024-04-17 10:04
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class NetherlandsFlag {

    public static void main(String[] args) {
        int[] nums = new int[]{2,0,2,1,1,0};
        int[] expect = new int[]{0,0,1,1,2,2};
        sortColors(nums);
        AlgorithmUtils.printArray(nums);
        assertTrue(AlgorithmUtils.arrayEquals(nums, expect));
    }

    /**
     * 数组中比num小的都放左边, 比num大的都放右边, 等于num的放中间
     * @param nums
     */
    public static void sortColors(int[] nums) {
        int targetValue = 1;
        int lessIndex = -1; //比1小的区域
        int moreIndex = nums.length; //比1大的区域
        int current = 0; // 当前位置
        while (current < moreIndex) {
            if (nums[current] == targetValue) {
                current++;
            }else if (nums[current] < targetValue) {
                swap(nums, current++, ++lessIndex);
            }else {
                swap(nums, current, --moreIndex);
            }
        }
    }

    public static void swap(int[] nums, int i, int j) {
        if ( i == j) {
            return;
        }
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }
}
