package com.loserico.algorithm_msb;

import com.loserico.common.lang.utils.AlgorithmUtils;

/**
 * 荷兰国旗
 * 给定一个数组, 一个目标数, 比目标数小的都放左边, 比目标数大的都放右边
 * <p>
 * 1. 当前数<目标数, 当前数和<=区的下一个数(索引++)交换, <=区向右扩, 当前数跳下一个
 * 2. 当前数=目标数, 当前数直接跳下一个
 * 3. 当前数>目标数, 当前数和>区前一个数交换, >区域向左扩, 当前数不动
 * 4. 当前数和有边界装上时停
 * <p>
 * Copyright: Copyright (c) 2024-04-16 11:02
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PartitionAndQuickSort {

    public static void main(String[] args) {
        /*int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        int index = partition(arr, 0, arr.length - 1);
        System.out.println(index);
        AlgorithmUtils.printArray(arr);*/

        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        int[] indeies = netherlandsFlag(arr, 0, arr.length - 1);
        AlgorithmUtils.printArray(indeies);
        AlgorithmUtils.printArray(arr);
    }

    /**
     * 荷兰国旗
     * 给定一个数组, 一个目标数, 比目标数小的都放左边, 比目标数大的都放右边, 返回中间划分位置的索引
     * arr[L..R]上，以arr[R]位置的数做划分值
     * <p>
     * 1. 当前数<目标数, 当前数和<=区的下一个数(索引++)交换, <=区向右扩, 当前数跳下一个
     * 2. 当前数=目标数, 当前数直接跳下一个
     * 3. 当前数>目标数, 当前数和>区前一个数交换, >区域向左扩, 当前数不动
     * 4. 当前数和有边界装上时停
     *
     * @param arr
     * @param l
     * @param r
     * @return
     */
    public static int partition(int[] arr, int l, int r) {
        if (l > r) {
            return -1;
        }
        if (l == r) {
            return l;
        }

        int lessEqual = l - 1; // <=区的边界
        int index = l;
        while (index < r) {
            //当前数<目标数, 当前数和<=区的下一个数(索引++)交换, <=区向右扩, 当前数跳下一个
            if (arr[index] <= arr[r]) {
                swap(arr, index++, ++lessEqual);
            }
        }
        swap(arr, ++lessEqual, r);
        return lessEqual;
    }

    public static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }

        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 荷兰国旗
     * 给定一个数组, 一个目标数, 比目标数小的都放左边, 等于目标数的放中间, 比目标数大的都放右边
     *
     * 1. 当前数<目标数, 当前数和<=区的下一个数(索引++)交换, <=区向右扩, 当前数跳下一个
     * 2. 当前数=目标数, 当前数直接跳下一个
     * 3. 当前数>目标数, 当前数和>区前一个数交换, >区域向左扩, 当前数不动
     * 4. 当前数和有边界撞上时停
     * @param arr
     * @param l
     * @param r
     * @return int[]
     */
    public static int[] netherlandsFlag(int[] arr, int l, int r) {
        if (l > r) {
            return new int[]{-1, -1};
        }
        if (l == r) {
            return new int[]{l, r};
        }

        int less = l - 1; // <区的右边界
        int more = r; //>区的左边界
        int index = l; //当前值
        while (index < more) {// 当前位置, 不能和 >区的左边界撞上
            /*
             * 1. 当前数<目标数, 当前数和<=区的下一个数(索引++)交换, <=区向右扩, 当前数跳下一个
             * 2. 当前数=目标数, 当前数直接跳下一个
             * 3. 当前数>目标数, 当前数和>区前一个数交换, >区域向左扩, 当前数不动
             * 4. 当前数和有边界撞上时停
             */
            if (arr[index] == arr[r]) {
                index++;
            } else if (arr[index] < arr[r]) {
                swap(arr, index++, ++less);
            } else {
                swap(arr, index, --more);
            }
        }
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }
}
