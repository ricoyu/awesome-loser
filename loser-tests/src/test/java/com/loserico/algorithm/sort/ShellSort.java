package com.loserico.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-07-02 15:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ShellSort {
	
	public static void main(String[] args) {
		int[] a = {26, 53, 67, 48, 57, 13, 48, 32, 60, 50};
		sort(a);
	}
	
	public static void sort(int[] a) {
		int temp;
		for (int increment = a.length / 2; increment > 0; increment /= 2) { //increment是步长, 第一次是5
			for (int i = increment; i < a.length; i++) { // i第一次是5, 第二次是6
				for (int j = i; j >= increment; j -= increment) { //j第一次也是5
					if (a[j - increment] > a[j]) {//第一次交换a[0], a[5], 第二次交换a[1], a[6]
						temp = a[j - increment];
						a[j - increment] = a[j];
						a[j] = temp;
					}
				}
			}
			System.out.println("步长为" + increment + "的排序结果");
			for (int i = 0; i < a.length; i++) {
				System.out.println(a[i]);
			}
		}
	}
}
