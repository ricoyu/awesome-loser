package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.*;

/**
 * 题目描述 <p/>
 * 100个人围成一圈，每个人有一个编号，编号从1开始到100。他们从1开始依次报数，报到为M的人自动退出圈圈，然后下一个人接着从1开始报数，直到剩余的人数小于M。请问最后剩余的人在原先的编号为多少？
 * <p/>
 * 输入描述 <p/>
 * 输入一个整数参数M。
 * <p/>
 * 输出描述 <p/>
 * 如果输入参数M小于等于1或者大于等于100，输出"ERROR!"；否则按照原先的编号从小到大的顺序，以逗号分割输出编号字符串。
 * <p/>
 * 示例1 <br/>
 * 输入: 3
 * <p/>
 * 输出: 58,91
 * <p/>
 * 说明: 输入M为3，最后剩下两个人
 * <p/>
 * 示例2 <p/>
 * 输入: 4
 * <p/>
 * 输出: 34,45,97
 * <p/>
 * 说明: 输入M为4，最后剩下三个人
 * <p/>
 * Copyright: Copyright (c) 2024-10-04 8:51
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class JosephusProblem {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入M: ");
		int m = scanner.nextInt();
		if (m < 1 || m > 100) {
			System.out.println("ERROR!");
			System.exit(1);
		}
		List<Integer> people
				= new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			people.add(i); // 初始化100个人的编号
		}

		int index = 0; //从第一个人开始报数
		while (people.size() >= m) {
			index = (index + m - 1) % people.size();// 计算要移除的人的索引位置
			people.remove(index); // 移除报到M的人
		}

		// 输出剩余的人的编号，使用逗号分隔
		System.out.println(String.join(", ", people.stream().map(String::valueOf).collect(toList())));
	}
}
