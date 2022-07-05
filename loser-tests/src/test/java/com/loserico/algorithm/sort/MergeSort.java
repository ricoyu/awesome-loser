package com.loserico.algorithm.sort;


import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2022-07-04 14:37
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MergeSort {
	
	public static void main(String[] args) {
		int[] data = {9, 5, 6, 8, 0, 3, 7, 1};
		mergeSort(data, 0, data.length-1);
		System.out.println(Arrays.toString(data));
	}
	
	/**
	 * @param data
	 * @param left  数组的左端
	 * @param right 数组的右端
	 */
	public static void mergeSort(int[] data, int left, int right) {
		if (left < right) { //相等了就表示只有一个数了, 不用再拆了
			int mid = (left + right) / 2;
			mergeSort(data, left, mid);
			mergeSort(data, mid + 1, right);
			//分完了接下来就要进行合并, 也就是递归里面归的过程
			merge(data, left, mid, right);
		}
		
	}
	
	public static void merge(int[] data, int left, int mid, int right) {
		int[] temp = new int[data.length]; //借助一个临时数组用来保存合并的数据
		int point1 = left; //表示是左边的第一个数的位置
		int point2 = mid+1; //表示的是右边第一个数的位置
		
		int loc = left; // 表示当前我们已经到了哪个位置
		while(point1 <= mid && point2 <= right) {
			if (data[point1] < data[point2]) {
				temp[loc] = data[point1];
				point1++;
				loc++;
			} else {
				temp[loc] = data[point2];
				point2++;
				loc++;
			}
		}
		while(point1<=mid) {
			temp[loc++] = data[point1++];
		}
		while (point2 <= right) {
			temp[loc++] = data[point2++];
		}
		for (int i = left; i <= right; i++) {
			data[i] = temp[i];
		}
	}
}
