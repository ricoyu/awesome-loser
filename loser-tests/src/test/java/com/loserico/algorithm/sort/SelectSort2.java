package com.loserico.algorithm.sort;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2023-05-30 上午10:31
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class SelectSort2 {
	
	public static void main(String[] args) {
		int[] arr = new int[]{2,1,3,5,4,8,6,5};
		for (int i = 0; i < arr.length; i++) {
			int minIndex = i;
			for (int j = i+1; j < arr.length; j++) {
				if (arr[j] < arr[minIndex]) {
					minIndex = j;
				}
			}
			swap(arr, i, minIndex);
		}
		
		System.out.println(Arrays.toString(arr));
	}
	
	private static void swap(int[] arr, int i, int minIndex) {
		int temp  = arr[i];
		arr[i] = arr[minIndex];
		arr[minIndex] = temp;
	}
}
