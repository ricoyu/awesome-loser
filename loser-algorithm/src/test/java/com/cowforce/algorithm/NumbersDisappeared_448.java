package com.cowforce.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-11-30 15:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NumbersDisappeared_448 {
	
	public List<Integer> findDisappearedNumbers(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			int index = Math.abs(nums[i]) -1;
			if (nums[index] > 0) {
				nums[index] = 0-nums[index];
			}
		}
		
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > 0) {
				result.add(i+1);
			}
		}
		
		return result;
	}
}
