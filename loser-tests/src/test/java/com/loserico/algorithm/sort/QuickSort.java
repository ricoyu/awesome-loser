package com.loserico.algorithm.sort;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2022-07-05 9:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class QuickSort {
	
	public static void main(String[] args) {
		int[] data = {45, 28, 80, 90, 50, 16, 100, 10};
		quickSort(data, 0, data.length-1);
		System.out.println(Arrays.toString(data));
	}
	
	public static void quickSort(int[] data, int left, int right) {
		int base = data[left]; //基准数
		int ll = left; //表示的是从左边开始找的位置
		int rr = right; //表示的是从右边开始找的位置
		while (ll < rr) {
			//从后面往前找比基数小的数
			while (ll < rr && data[rr] >= base) {
				rr--;
			}
			if (ll < rr) {
				int temp = data[rr];
				data[rr] = data[ll];
				data[ll] = temp;
				ll++;
			}
			while (ll < rr && data[ll] <= base) {
				ll++;
			}
			if (ll < rr) {
				int temp = data[rr];
				data[rr] = data[ll];
				data[ll] = temp;
				rr--;
			}
		}
		//分成了三部分, 左右继续快排
		if (left < ll) {
			quickSort(data, left, ll - 1);
		}
		if (rr < right) {
			quickSort(data, ll + 1, right);
		}
	}
}
