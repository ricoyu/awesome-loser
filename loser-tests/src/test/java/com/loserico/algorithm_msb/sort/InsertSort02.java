package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;
import org.junit.Test;

public class InsertSort02 {

    @Test
    public void test() {
        int[] arr = AlgorithmUtils.randomArr(10, 20);
        AlgorithmUtils.printArray(arr);
        insertSort(arr);
        AlgorithmUtils.printArray(arr);
    }
    public static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int num = arr[i];
            int j = i-1;
            while (j >=0 && arr[j] > num) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = num;
        }
    }
}
