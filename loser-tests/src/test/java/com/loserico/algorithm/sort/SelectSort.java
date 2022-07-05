package com.loserico.algorithm.sort;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2022-07-04 20:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SelectSort {
	
	public static void main(String[] args) {
		int[] a = {4,5,6,3,2,1};
		int n = a.length;
		for (int i = 0; i < n; i++) {
			int minIndex = i;
			for (int j = i+1; j < n; j++) {
				if (a[j]< a[minIndex]) {
					minIndex = j;
				}
			}
			swap(a, i, minIndex);
		}
		System.out.println(Arrays.toString(a));
	}
	
	private static void swap(int[] a, int i, int minIndex) {
		int temp = a[i];
		a[i] = a[minIndex];
		a[minIndex] = temp;
	}
}
