package com.loserico.algorithm.leetcode;

import com.loserico.common.lang.utils.Arrays;

import java.util.Scanner;

/**
 * 合并两个有序数组
 * <p/>
 * 给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。
 * <p/>
 * 请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。
 * <p/>
 * 注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。为了应对这种情况，nums1 的初始长度为 m + n，其中前 m 个元素表示应合并的元素，后 n 个元素为 0 ，应忽略。nums2 的长度为 n 。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3 <br/>
 * 输出：[1,2,2,3,5,6] <br/>
 * 解释：需要合并 [1,2,3] 和 [2,5,6] 。 <br/>
 * 合并结果是 [1,2,2,3,5,6] ，其中斜体加粗标注的为 nums1 中的元素。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：nums1 = [1], m = 1, nums2 = [], n = 0 <br/>
 * 输出：[1] <br/>
 * 解释：需要合并 [1] 和 [] 。 <br/>
 * 合并结果是 [1] 。
 * <p/>
 * 示例 3：
 * <p/>
 * 输入：nums1 = [0], m = 0, nums2 = [1], n = 1 <br/>
 * 输出：[1] <br/>
 * 解释：需要合并的数组是 [] 和 [1] 。 <br/>
 * 合并结果是 [1] 。 <br/>
 * 注意，因为 m = 0 ，所以 nums1 中没有元素。nums1 中仅存的 0 仅仅是为了确保合并结果可以顺利存放到 nums1 中
 * <p/>
 * 要实现这个合并有序数组的问题，我们可以从尾部开始合并，因为 nums1 的后部有足够的空间容纳 nums2 的所有元素。
 * 这样可以避免在数组前部插入元素时的元素移动操作，提高效率。我们将从 nums1 和 nums2 的末尾开始比较大小，
 * 把较大的元素放在 nums1 的最后一个位置，然后依次向前填充。
 *
 * 解题思路
 * <ul>初始化三个指针
 *     <li/>p1 指向 nums1 中前 m 个元素的末尾（即 m - 1）。
 *     <li/>p2 指向 nums2 的末尾（即 n - 1）。
 *     <li/>p 指向 nums1 的总长度末尾（即 m + n - 1）
 * </ul>
 * 从 p 开始向前填充元素，比较 p1 和 p2 位置上的元素，将较大的元素放到 p 位置，且将相应指针前移。 <br/>
 * 如果 p2 先处理完，说明所有 nums2 中的元素都已放入 nums1，合并完成。 <br/>
 * 如果 p1 先处理完，仍需要将剩下的 nums2 元素放入 nums1 的前面位置。 <br/>
 * Copyright: Copyright (c) 2024-11-02 9:38
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MergeSortedArrays {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入nums1: ");
		String[] parts = scanner.nextLine().trim().split(",");
		System.out.print("请输入m: ");
		int m = scanner.nextInt();
		System.out.print("请输入nums2: ");
		scanner.nextLine();
		String[] parts2 = scanner.nextLine().trim().split(",");
		System.out.print("请输入n: ");
		int n = scanner.nextInt();
		int[] nums1 = new int[m+n];
		for(int i = 0; i < m; i++) {
		  nums1[i] = Integer.parseInt(parts[i].trim());
		}
		int[] nums2 = new int[n];
		for(int i = 0; i < n; i++) {
		  nums2[i] = Integer.parseInt(parts2[i].trim());
		}

		merge(nums1, m, nums2, n);
		Arrays.print(nums1);
	}

	public static void merge(int[] nums1, int m, int[] nums2, int n) {
		// 初始化三个指针，分别指向 nums1 和 nums2 的末尾，以及合并后数组的末尾
		int p1 = m-1;
		int p2 = n-1;
		int p = (m+n)-1;

		// 从后往前填充 nums1，以避免覆盖未处理的元素
		while (p1>=0 && p2>=0) {
			// 比较 nums1[p1] 和 nums2[p2]，将较大值放在 nums1[p] 位置
			if (nums1[p1] > nums2[p2]) {
				nums1[p] = nums1[p1];
				p1--;// p1 向左移动
			} else {
				nums1[p] = nums2[p2];
				p2--; // p2 向左移动
			}
			p--; // p 向左移动
		}

		// 如果 nums2 中仍有剩余元素，将其复制到 nums1 中
		// 仅当 nums2 中还有元素时才需要执行此步骤
		while (p2>=0) {
			nums1[p] = nums2[p2];
			p2--;
			p--;
		}
	}
}
