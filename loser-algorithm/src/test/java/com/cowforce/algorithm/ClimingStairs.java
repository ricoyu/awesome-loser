package com.cowforce.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 注意：给定 n 是一个正整数。
 * <p>
 * Copyright: (C), 2022-11-28 20:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClimingStairs {
	
	/**
	 * 递归的解法
	 *
	 * @param n
	 * @return int
	 */
	public int climbStairs1(int n) {
		if (n == 1) {
			return 1; //一阶台阶只有一种走法
		}
		if (n == 2) {
			return 2; //两节台阶有两种走法, 一次走一阶和一次走两阶
		}
		return climbStairs1(n - 1) + climbStairs1(n - 2);
	}
	
	Map<Integer, Integer> storeMap = new HashMap<>();
	
	/**
	 * 递归的解法,用HashMap存储中间计算结果
	 *
	 * @param n
	 * @return
	 */
	public int climbStairs2(int n) {
		if (n == 1) {
			return 1; //一阶台阶只有一种走法
		}
		if (n == 2) {
			return 2; //两节台阶有两种走法, 一次走一阶和一次走两阶
		}
		
		if (storeMap.get(n) != null) {
			return storeMap.get(n);
		} else {
			int result = climbStairs2(n - 1) + climbStairs2(n - 2);
			storeMap.put(n, result);
			return result;
		}
	}
	
	public int climbStairs3(int n) {
		if (n == 1) {
			return 1; //一阶台阶只有一种走法
		}
		if (n == 2) {
			return 2; //两节台阶有两种走法, 一次走一阶和一次走两阶
		}
		int result = 0;
		int pre = 2;
		int prePre = 1;
		for (int i = 3; i < n; i++) {
			result = pre + prePre;
			prePre = pre;
			pre = result;
		}
		return result;
	}
}
