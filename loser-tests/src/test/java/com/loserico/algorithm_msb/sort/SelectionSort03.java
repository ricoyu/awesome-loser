package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

public class SelectionSort03 {

    @Test
    public void test() {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        selectionsort(arr);
        AlgorithmUtils.printArray(arr);
    }

    public static void selectionsort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                AlgorithmUtils.swap(arr, minIndex, i);
            }
        }
    }
}
