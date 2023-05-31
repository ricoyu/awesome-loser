package com.loserico.algorithm.sort;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2023-05-30 上午11:03
 * <p>
 * <p>
 * Company: Bizgo.
 *
 * @author Rico Yu yuxuehua@bizgo.com
 * @version 1.0
 */
public class SortDemo {
	
	public static void main(String[] args) {
		int[] a = {6, 5, 4, 3, 2, 1, 9, 8, 10, 7, 0, 11, 12};
		print(a);
		//quicksort(a);
		//bubbleSort(a);
		//insertSort(a);
		selectSort(a);
		print(a);
	}
	
	public static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void selectSort(int[] a) {
		if (a == null || a.length < 2) {
			return;
		}
		for (int i = 0; i < a.length; i++) {
			int minValueIndex = i;
			for (int j = minValueIndex + 1; j < a.length; j++) {
				minValueIndex = a[j] < a[minValueIndex] ? j : minValueIndex;
			}
			swap(a, i, minValueIndex);
		}
	}
	
	public static void insertSort(int[] a) {
		if (a == null || a.length < 2) {
			return;
		}
		
		int n = a.length;
		for (int i = 1; i < n; i++) {
			int minIndex = i;
			while ((minIndex - 1) >= 0 && a[minIndex - 1] > a[minIndex]) {
				swap(a, minIndex - 1, minIndex);
				minIndex--;
			}
		}
	}
	
	public static void bubbleSort(int[] a) {
		if (a == null || a.length < 2) {
			return;
		}
		int n = a.length - 1;
		for (int i = n; i >= 0; i--) {
			for (int j = 0; j < n; j++) {
				if (a[j] > a[j + 1]) {
					swap(a, j, j + 1);
				}
			}
		}
	}
	
	public static void quicksort(int[] a) {
		if (a == null || a.length < 2) {
			return;
		}
		for (int i = 0; i < a.length; i++) {
			int minIndex = i;
			for (int j = i + 1; j < a.length; j++) {
				minIndex = a[j] < a[minIndex] ? j : minIndex;
			}
			swap(a, i, minIndex);
		}
	}
	
	
	public static void print(int[] a) {
		System.out.println(Arrays.toString(a));
	}
}
