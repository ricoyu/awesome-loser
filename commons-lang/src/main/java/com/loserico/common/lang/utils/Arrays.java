package com.loserico.common.lang.utils;

import java.util.Objects;

import static java.util.Arrays.stream;

public final class Arrays {

    @SuppressWarnings("unchecked")
    public static final <T> T[] asArray(T... args) {
        return args;
    }

    /**
     * 过滤Array中的null元素并排序
     *
     * @param args
     * @return T[]
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] nonNull(T... args) {
        if (args == null) {
            return null;
        }
        return (T[]) stream(args)
                .filter(Objects::nonNull)
                .sorted()
                .distinct()
                .toArray();
    }

    /**
     * 打印数组
     *
     * @param array
     */
    public static void print(int[] array) {
        for (int i : array) {
            System.out.print(i + "  ");
        }
        System.out.println("");
    }

    /**
     * 产生一个随机的数组, 数组长度也是随机的, 但不超过maxSize, 数组元素随机, 最大值为maxValue
     *
     * @param maxSize
     * @param maxValue
     * @return
     */
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        // Math.random() -> [0,1) 所有的小数，等概率返回一个
        // Math.random() * N -> [0,N) 所有小数，等概率返回一个
        // (int)(Math.random() * N) -> [0,N-1] 所有的整数，等概率返回一个
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())]; // 长度随机
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    /**
     * 在原数组基础上复制一个数组
     *
     * @param arr
     * @return
     */
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 如果两个数组长度不一样, 或者元素不一样, 返回false
     * 两个数组都为null返回true
     *
     * @param arr1
     * @param arr2
     * @return boolean
     */
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }
}
