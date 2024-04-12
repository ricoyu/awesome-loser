package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.Arrays;
import org.junit.Test;

public class InsertionSort {

    public void insertSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = 1; i < arr.length; i++) {
            int num = arr[i];
            int j = i -1;
            while(j>=0 && arr[j] > num) {
                arr[j+1] = arr[j];
                j--;
            }
            //插入key到正确的位置
            arr[j+1] = num;
        }
    }

    @Test
    public void testInsertSort() {
    	int[][] arrays = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {9, 8, 7, 6, 5, 4, 3, 2, 1},
            {4, 2, 7, 1, 9, 5, 6, 3, 8},
            {5, 2, 5, 3, 5, 6, 1, 9, 2},
            {1, 3, 2, 5, 4, 7, 6, 9, 8},
            {1, 2, 3, 1, 2, 3, 1, 2},
            {102, 211, 123, 654, 378, 289, 456, 123, 741}
        };

        for (int i = 0; i < arrays.length; i++) {
            int[] arr = arrays[i];
            insertSort(arr);
            Arrays.print(arr);
        }
    }
}
