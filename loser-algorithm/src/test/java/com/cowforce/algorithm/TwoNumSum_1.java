package com.cowforce.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个整数目标值 target, 请你在该数组中找出 和为目标值 target 的那 两个整数, 并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * <p>
 * Copyright: (C), 2022-11-30 9:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TwoNumSum_1 {
	
	public int[] twoSum(int[] nums, int target) {
		int[] result = new int[2];
		for (int i = 0; i < nums.length; i++) {
			for (int j = i+1; j < nums.length; j++) {
				if ((nums[i]+nums[j]) == target) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return result;
	}
	
	public int[] twoSum2(int[] nums, int target) {
		Map<Integer, Integer> storeMap = new HashMap<>();
		int[] result = new int[2];
		for (int i = 0; i < nums.length; i++) {
			int diff = target - nums[i];
			Integer anotherIndex = storeMap.get(diff);
			if (anotherIndex != null) {
				result[0] = i;
				result[1] = anotherIndex;
				return result;
			} else {
				storeMap.put(nums[i], i);
			}
		}
		return result;
	}
}
