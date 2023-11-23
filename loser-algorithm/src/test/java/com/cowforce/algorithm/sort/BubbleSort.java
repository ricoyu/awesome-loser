package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-08 12:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BubbleSort {
	
	public static void sort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length-1-i; j++) {
				if (arr[j] >= arr[j+1]) {
					int tmp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = tmp;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		int[] data = PrintArray.SRC;
		PrintArray.print(data);
		sort(data);
		PrintArray.print(data);
	}
}
