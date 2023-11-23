package com.cowforce.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-01-31 15:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
	public class ThreeSum_15 {
	
	/**
	 * 排序 + 双指针解法
	 *
	 * @param nums
	 * @return
	 */
	public List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> results = new LinkedList<>();
		
		//长度小于3,就不存在三数之和了
		if (nums.length < 3) {
			return results;
		}
		//排序
		Arrays.sort(nums);
		int n = nums.length;
		for (int i = 0; i < n; i++) {
			//如果第一个数就大于0，那就没必要再查找下去了
			if (nums[0] > 0) {
				break;
			}
			int head = i + 1, tail = n - 1;
			//head指针和tail指针相对而行，但必须保证head在tail的左边
			while (head < tail) {
				//三数之和有三种情况：大于0，小于0，等于0
				int sum = nums[i] + nums[head] + nums[tail];
				if (sum < 0) {
					head++;//三数之和小于0，左边head的那个数太小了，要把它向右移
				} else if (sum > 0) {
					tail--;//三数之和大于0，tail坐标的那个数太大了，要把它向左移
				} else {
					//三数之和等于0，添加到返回列表里面，并且同时将左指针右移，右指针左移，探索下一组适合的数据
					List<Integer> list = new LinkedList<>();
					list.add(nums[i]);
					list.add(nums[head]);
					list.add(nums[tail]);
					results.add(list);
					//去重
					while (head + 1 <= tail && nums[head] == nums[head + 1]) {
						head++;
					}
					while (head + 1 <= tail && nums[tail] == nums[tail - 1]) {
						tail--;
					}
					head++;
					tail--;
				}
			}
			//去重
			while (i + 1 < n && nums[i + 1] == nums[i]) {
				i++;
			}
			
		}
		//如果第一个数就大于0，那就没必要再查找下去了
		
		return results;
	}
}
