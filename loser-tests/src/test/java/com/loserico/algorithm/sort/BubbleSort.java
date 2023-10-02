package com.loserico.algorithm.sort;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2022-07-04 20:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BubbleSort {
	
	public static void main(String[] args) {
		int[] data = {4,5,6,3,2,1};
		int n = data.length;
		
		for (int i = 0; i < n - 1; i++) { //排序的次数
			boolean flag = false;
			for (int j = 0; j < n-1 -i; j++) {
				if (data[j] > data[j+1]) {
					int temp = data[j];
					data[j] = data[j+1];
					data[j+1] = temp;
					flag = true;
				}
			}
			if (!flag) {
				break;
			}
		}
		System.out.println(Arrays.toString(data));
	}
	
	
}
