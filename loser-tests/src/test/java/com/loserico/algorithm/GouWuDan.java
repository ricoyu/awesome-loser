package com.loserico.algorithm;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GouWuDan {
	
	/**
	 * 购物单
	 * <p>
	 * 题目描述
	 * 王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品分为两类：主件与附件，附件是从属于某个主件的，下表就是一些主件与附件的例子：
	 * 主件	附件
	 * 电脑	打印机，扫描仪
	 * 书柜	图书
	 * 书桌	台灯，文具
	 * 工作椅	无
	 * 如果要买归类为附件的物品，必须先买该附件所属的主件。每个主件可以有 0 个、 1 个或 2 个附件。附件不再有从属于自己的附件。王强想买的东西很多，为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示，第 5 等最重要。他还从因特网上查到了每件物品的价格（都是 10 元的整数倍）。他希望在不超过 N 元（可以等于 N 元）的前提下，使每件物品的价格与重要度的乘积的总和最大。
	 * 设第 j 件物品的价格为 v[j] ，重要度为 w[j] ，共选中了 k 件物品，编号依次为 j 1 ， j 2 ，……， j k ，则所求的总和为：
	 * v[j 1 ]*w[j 1 ]+v[j 2 ]*w[j 2 ]+ … +v[j k ]*w[j k ] 。（其中 * 为乘号）
	 * 请你帮助王强设计一个满足要求的购物单。
	 * <p>
	 * 输入描述:
	 * 输入的第 1 行，为两个正整数，用一个空格隔开：N m
	 * <p>
	 * （其中 N （ <32000 ）表示总钱数， m （ <60 ）为希望购买物品的个数。）
	 * <p>
	 * 从第 2 行到第 m+1 行，第 j 行给出了编号为 j-1 的物品的基本数据，每行有 3 个非负整数 v p q
	 * <p>
	 * （其中 v 表示该物品的价格（ v<10000 ）， p 表示该物品的重要度（ 1 ~ 5 ）， q 表示该物品是主件还是附件。如果 q=0 ，表示该物品为主件，如果 q>0 ，表示该物品为附件， q 是所属主件的编号）
	 * <p>
	 * <p>
	 * 输出描述:
	 * 输出文件只有一个正整数，为不超过总钱数的物品的价格与重要度乘积的总和的最大值（ <200000 ）。
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int m = sc.nextInt();
		int n = sc.nextInt();
		int[] v = new int[n + 1];
		int[] p = new int[n + 1];
		int[] q = new int[n + 1];
		int groups = 0;
		for (int i = 1; i <= n; i++) {
			v[i] = sc.nextInt();
			p[i] = sc.nextInt();
			q[i] = sc.nextInt();
			if (q[i] == 0) {
				groups++;
			}
		}
		
		//分组
		int[][] _v = new int[groups + 1][4];
		int[][] _p = new int[groups + 1][4];
		processData(q, v, p, _v, _p);
		
		int gc = _v.length;
		int[][] r = new int[gc][m + 1];
		for (int i = 1; i < gc; i++) {
			for (int j = 1; j <= m; j++) {
				
				int max = r[i - 1][j];
				for (int t = 1; t < _v[i].length; t++) {
					int tempv = _v[i][t];
					int tempp = _p[i][t];
					if (tempv != 0 && tempv <= j) {
						max = Math.max(max, r[i - 1][j - tempv] + tempp);
					}
				}
				r[i][j] = max;
			}
		}
		System.out.println(r[gc - 1][m]);
	}
	
	private static void processData(int[] m, int[] v, int[] p, int[][] _v, int[][] _p) {
		Map<Integer, List<Integer>> groups = new HashMap<>();
		for (int i = 1; i < m.length; i++) {
			if (m[i] == 0) {
				if (!groups.containsKey(i)) {
					List<Integer> temp = new ArrayList<Integer>();
					temp.add(i);
					groups.put(i, temp);
				}
				
			} else {
				if (groups.containsKey(m[i])) {
					List<Integer> list = groups.get(m[i]);
					list.add(i);
				} else {
					List<Integer> temp = new ArrayList<Integer>();
					temp.add(m[i]);
					temp.add(i);
					groups.put(m[i], temp);
				}
			}
		}
		int index = 1;
		for (List<Integer> list : groups.values()) {
			int size = list.size();
			if (size == 1) {
				_v[index][1] = v[list.get(0)];
				_p[index][1] = p[list.get(0)] * v[list.get(0)];
			} else if (size == 2) {
				_v[index][1] = v[list.get(0)];
				_p[index][1] = p[list.get(0)] * v[list.get(0)];
				
				_v[index][2] = v[list.get(0)] + v[list.get(1)];
				_p[index][2] = p[list.get(0)] * v[list.get(0)] + p[list.get(1)] * v[list.get(1)];
			} else {
				_v[index][1] = v[list.get(0)];
				_p[index][1] = p[list.get(0)] * v[list.get(0)];
				
				_v[index][2] = v[list.get(0)] + v[list.get(1)];
				_p[index][2] = p[list.get(0)] * v[list.get(0)] + p[list.get(1)] * v[list.get(1)];
				
				_v[index][3] = v[list.get(0)] + v[list.get(1)] + v[list.get(2)];
				_p[index][3] = p[list.get(0)] * v[list.get(0)] + p[list.get(1)] * v[list.get(1)] + p[list.get(2)] * v[list.get(2)];
			}
			index++;
		}
	}
	
}