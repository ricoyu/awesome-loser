package com.loserico.algorithm.leetcode;

import java.util.Arrays;
import java.util.Scanner;

/**
 * H 指数
 * <p/>
 * 给你一个整数数组 citations，其中 citations[i] 表示研究者的第 i 篇论文被引用的次数。计算并返回该研究者的 h 指数。
 * <p/>
 * 根据维基百科上 h 指数的定义：h 代表“高引用次数” ，一名科研人员的 h 指数 是指他（她）至少发表了 h 篇论文，并且 至少 有 h 篇论文被引用次数大于等于 h 。
 * 如果 h 有多种可能的值，h 指数 是其中最大的那个。
 * <p/>
 * 示例 1：
 * <p/>
 * 输入：citations = [3,0,6,1,5] <br/>
 * 输出：3 <br/>
 * 解释：给定数组表示研究者总共有 5 篇论文，每篇论文相应的被引用了 3, 0, 6, 1, 5 次。
 * 由于研究者有 3 篇论文每篇 至少 被引用了 3 次，其余两篇论文每篇被引用 不多于 3 次，所以她的 h 指数是 3。
 * <p/>
 * 示例 2：
 * <p/>
 * 输入：citations = [1,3,1] <br/>
 * 输出：1 <br/>
 * <p/>
 * <ol>解题思路
 *     <li/>排序：首先将 citations 数组从大到小排序，以方便我们检查每篇论文的引用次数。
 *     <li/>遍历数组：排序后，逐一检查每篇论文的引用次数，判断是否满足 h 指数的条件。
 *     <li/>判断条件：对于排序后的数组中第 i 篇论文，如果该论文的引用次数 citations[i] 大于等于 i + 1，
 *          则说明存在至少 i + 1 篇论文的引用次数大于等于 i + 1，我们可以继续检查下一篇论文。
 *     <li/>终止条件：当发现第 i 篇论文的引用次数小于 i + 1 时，说明 h 指数就是 i，因为已经没有更多的论文满足 h 指数的条件。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-10 12:21
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class HIndexCalculator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入数组: ");
		String input = scanner.nextLine().trim();
		String[] parts = input.split(",");
		int[] citations = new int[parts.length];
		for(int i = 0; i < parts.length; i++) {
		  citations[i] = Integer.parseInt(parts[i].trim());
		}
		System.out.println(calculateHIndex(citations));
	}

	public static int calculateHIndex(int[] citations) {
		Integer[] array = Arrays.stream(citations).boxed().toArray(Integer[]::new);
		Arrays.sort(array, (a, b) -> {
			return b - a;
		});

		// 遍历排序后的数组，寻找 h 指数
		for (int i = 0; i < array.length; i++) {
			// 检查引用次数是否大于等于当前的 i + 1
			if (array[i] < i + 1) {
				// 一旦发现某篇论文的引用次数小于 i + 1，返回 i 即为 h 指数
				return i;
			}
		}

		// 如果所有论文的引用次数都满足条件，h 指数等于论文数量
		return array.length;
	}
}
