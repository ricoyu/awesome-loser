package com.loserico.algorithm.sort;

/**
 * 插入排序
 * <p>
 * Copyright: (C), 2022-07-02 12:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InsertSort {
	
	public static void main(String[] args) {
		int[] a = {9, 8 ,7 , 0 ,1, 3, 2};
		int n = a.length;
		/*
		 * 这里面会有几层循环 2
		 * 时间复杂度 n^2
		 * 最好的情况O(n), 里面的循环每次都走到break分支, 即数组已经排好序了
		 */
		for (int i = 1; i < n; i++) { //从1开始是因为第一个数不用排序, 即这里从数字8开始插入排序
			int data = a[i]; //第一次data是8
			int j = i -1;
			for (;  j>=0 ; j--) { //从尾到头
				if (a[j] > data) { //第一次a[j]是9
					a[j+1] = a[j]; // 数据9往后移动一位, 覆盖了a[j+1]位置上的数, 没关系, 因为已经用data记录了
				} else { //因为前面已经是排好序的, 所以找到一个比它小的就不用再找了, 因为前面的肯定更小
					break;
				}
			}
			a[j+1] = data;
			System.out.println("第" +i+"次排序的结果是:");
			for (int k = 0; k < n; k++) {
				System.out.println(a[k]);
			}
		}
		
	}
}
