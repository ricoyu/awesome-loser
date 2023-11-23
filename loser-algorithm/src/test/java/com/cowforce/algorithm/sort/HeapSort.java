package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-14 10:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HeapSort {
	
	//用于记录数组array的长度
	private static int len;
	
	/**
	 * 交换数组内两个元素
	 * @param nums
	 * @param i
	 * @param j
	 */
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	public static int[] sort(int[] nums) {
		len = nums.length;
		if (len <=1) {
			return nums;
		}
		//构建一个最大堆
		buildMaxHeap(nums);
		//循环将堆首位（最大值）与未排序数据末位交换，然后重新调整为最大堆
		while (len >0) {
			swap(nums, 0, len-1);
			len --;
			adjustHeap(nums, 0);
			PrintArray.printTree(nums);
			System.out.println("--------------------");
		}
		return nums;
	}
	
	/**
	 * 对于堆, 有如下推论:
	 * 1. 对于位置为K的节点, 左子节点=2*k+1, 右子节点=2*(k+1)
	 * 2. 最后一个非叶子节点的位置为(N/2)-1, N为数组长度
	 * 建立最大堆
	 */
	public static void buildMaxHeap(int[] nums) {
		//从最后一个非叶子节点开始向上构造最大堆
		for (int i = (len/2-1); i >=0 ; i--) {
			adjustHeap(nums, i);
			PrintArray.printTree(nums);
		System.out.println("=================================");
		}
		System.out.println("构造完成最大堆");
		PrintArray.printTree(nums);
		System.out.println("============================================");
	}
	
	private static void adjustHeap(int[] nums, int i) {
		int maxIndex = i;
		int left = 2*i+1; //左孩子位置
		int right = 2*(i+1); //右孩子位置
		//如果有左子树，且左子树大于父节点，则将最大指针指向左子树
		if (left <len && nums[left] > nums[maxIndex]) {
			maxIndex = left;
		}
		//如果有右子树，且右子树大于父节点且大于左子树，则将最大指针指向右子树
		if (right < len && nums[right] > nums[maxIndex]) {
			maxIndex = right;
		}
		//如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
		if (maxIndex != i) {
			swap(nums, maxIndex, i);
			//PrintArray.print(nums);
			adjustHeap(nums, maxIndex);
		}
	}
	
	
	public static void main(String[] args) {
		/*System.out.println("原始数组");
		PrintArray.print(PrintArray.SRC);
		System.out.println("============================================");
		int[] dest = HeapSort.sort(PrintArray.SRC);
		PrintArray.print(dest);*/
		//int[] SRC = {86, 39, 77, 23, 32, 45, 58, 63, 93, 4, 37, 22};
		int[] SRC = {86, 39, 77, 23, 32, 45, 58, 63, 93, 4, 37};
		PrintArray.printTree(SRC);
	}
}
