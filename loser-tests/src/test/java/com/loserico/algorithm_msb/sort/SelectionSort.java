package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * 选择排序算法
 * <p>
 * Copyright: Copyright (c) 2024-03-18 9:19
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SelectionSort {

    /**
     * 首先在未排序序列中找到最小（大）元素, 存放到排序序列的起始位置
     * 再从剩余未排序元素中继续寻找最小（大）元素, 然后放到已排序序列的末尾
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        if ((arr==null || arr.length<2)) {
            return;
        }

        //外层循环控制排序趟数
        for (int i = 0; i < arr.length-1; i++) {
            // 记录最小元素的索引, 默认为当前趟数的第一个元素
            int minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                //如果找到比当前minIndex索引处值更小的元素，则更新minIndex
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            //如果minIndex不等于i, 说明找到了更小的元素, 交换之
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11}; // 待排序数组
        selectionSort(arr); // 调用选择排序方法
        AlgorithmUtils.printArray(arr);
    }
}
