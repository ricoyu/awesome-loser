package com.loserico.algorithm_msb.sort;

import org.junit.Test;

public class BubbleSort {

    @Test
    public void testBubbleSort() {
        // 初始化一个二维数组，包含三个一维数组：最优情况、最差情况和中等情况
        int[][] arrays = {
                {1,2,3,4,5,6,7,8,9}, // 最优情况（已经排序好）
                {9,8,7,6,5,4,3,2,1}, // 最差情况（完全逆序）
                {5,3,1,2,4,7,6,9,8}  // 中等情况（部分排序）
        };

        for (int[] arr : arrays) {
            bubblesort(arr);
            for (int i : arr) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    public static void bubblesort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            // 初始化一个布尔标记，用于判断这一轮是否进行了交换
            boolean swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            // 如果在这一轮没有进行任何交换，意味着数组已经是排序好的，可以直接退出循环
            if (!swapped) break;
        }
    }

    /**
     * 利用异或运算交换两个数
     * 异或运算的性质：
     * 任何数和自身做异或运算的结果为0
     * 任何数和0做异或运算的结果为其本身
     *
     * @param a
     * @param b
     */
    public static void swap(int[] arr, int i, int j) {
        // 检查要交换的两个元素是否位于不同的位置
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }
}
