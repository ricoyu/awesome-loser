package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * 荷兰国旗问题
 * 75 https://leetcode.cn/problems/sort-colors/description/
 *
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *
 * 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 *
 * 必须在不使用库内置的 sort 函数的情况下解决这个问题。
 * <p>
 * Copyright: Copyright (c) 2024-04-16 11:44
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class Solution {

    public static void main(String[] args) {
        int[] nums = new int[]{2,0,2,1,1,0};
        sortColors(nums);
        AlgorithmUtils.printArray(nums);
    }
    public static void sortColors(int[] nums) {
        if (nums == null ||nums.length==1) {
            return;
        }
        /*
         * 遍历数组，直到 mid 超过 high：
         *
         * 如果 nums[mid] 是 0（红色），就将它与 nums[low] 交换，然后 low 和 mid 都向右移动一位。
         * 如果 nums[mid] 是 1（白色），mid 向右移动一位。
         * 如果 nums[mid] 是 2（蓝色），就将它与 nums[high] 交换，然后 high 向左移动一位，mid 保持不变以检查交换过来的元素。
         */
        int low = 0; //它的左侧（不包括 low）都是红色（0）
        int mid = 0; //当前考察的元素的位置，起始时等于 low。
        int high = nums.length-1; //数组的结束位置, 它的右侧（不包括 high）都是蓝色（2）

        while (mid <=high) {
            switch (nums[mid]) {
                case 0: //红色
                    swap(nums, low++, mid++);
                    break;
                case 1:
                    mid++;
                    break;
                case 2:
                    swap(nums, mid, high);
                    high--;

            }
        }

    }

    public static void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        nums[i] = nums[i] ^nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }
}