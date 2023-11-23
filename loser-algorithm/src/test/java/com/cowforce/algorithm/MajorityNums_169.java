package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2023-01-28 21:27
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MajorityNums_169 {
	
	public int majorityElement(int[] nums) {
		//对拼消耗的数字
		int currentNum = nums[0];
		//对拼消耗的数字个数
		int count = 1;
		for (int i = 1; i < nums.length; i++) {
			if (count == 0) {
				//对拼消耗的数字设置为当前元素
				currentNum = nums[i];
				count = 1;
			} else {
				if (nums[i] == currentNum) {
					//对拼消耗的字数个数+1
					count++;
				} else {
					//数字不等, 兑子, 对拼消耗的数字个数减1
					count--;
				}
			}
		}
		 return currentNum;
	}
}
