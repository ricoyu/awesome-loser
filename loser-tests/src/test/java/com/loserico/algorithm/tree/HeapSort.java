package com.loserico.algorithm.tree;

import java.util.Arrays;

/**
 * <p>
 * Copyright: (C), 2022-07-09 10:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HeapSort {
	
	public static void main(String[] args) {
		int[] data = {8, 4, 20, 7, 3, 1, 25, 14, 17};
		heapsort(data);
		System.out.println(Arrays.toString(data));
	}
	
	/**
	 * 建一个大顶堆,end表示最多建到的点 logn
	 *
	 * @param data
	 * @param start
	 * @param end
	 */
	public static void maxHeap(int[] data, int start, int end) {
		int parent = start;
		int son = parent * 2 + 1; //下标是从0开始的就要加1，从1就不用
		while (son < end) {
			int temp = son;
			if (son + 1 < end && data[son] < data[son + 1]) {// 表示右节点比左节点大
				temp = son + 1;// 就要换右节点跟父节点
			}
			// temp表示的是 我们左右节点大的那一个
			if (data[parent] > data[temp]) {
				return; //不用交换
			} else {
				int t = data[parent];
				data[parent] = data[temp];
				data[temp] = t;
				parent = temp;  //继续堆化
				son = parent * 2 + 1;
			}
		}
	}
	
	public static void heapsort(int[] data) {
		int len = data.length;
		for (int i = len / 2 - 1; i >= 0; i--) {
			maxHeap(data, i, len);
		}
		for (int i = len - 1; i > 0; i--) {
			if (data[0] > data[i]) {
				int temp = data[0];
				data[0] = data[i];
				data[i] = temp;
				maxHeap(data, 0, i);
			}
		}
	}
}
