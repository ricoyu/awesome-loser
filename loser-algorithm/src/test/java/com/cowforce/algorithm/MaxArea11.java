package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2023-01-28 22:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MaxArea11 {
	public int maxArea(int[] height) {
		//i指向第一个元素, j指向最后一个元素
		int i = 0, j = height.length -1;
		int maxWater = 0;
		while (i < j) {
			//height[i]和height[j]谁小就移动谁
			maxWater = height[i] < height[j] ? Math.max(maxWater, (j-i) * height[i++]): 
					Math.max(maxWater, (j-i) * height[j--]); // height[i++]和hewight[j--]是移动两个指针的意思
		}
		return maxWater;
	}
}
