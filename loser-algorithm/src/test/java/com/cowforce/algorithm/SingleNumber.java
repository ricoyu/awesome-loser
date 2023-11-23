package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-16 15:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SingleNumber {
	
	public int singleNumber(int[] nums) {
		int result = 0; //这个值最后就是nums里面只出现过一次的那个数的值
		for (int i = 0; i < nums.length; i++) {
			/*
			 * 如果nums[i] 1= 0, 那么result就等于nums[i]
			 * 如果nums里面一个数出现了两次, 那么相应的位上数字都变成0, 最后的结果就是只出现一次的那个数
			 */
			result = result ^ nums[i];
		}
		return result;
	}
}
