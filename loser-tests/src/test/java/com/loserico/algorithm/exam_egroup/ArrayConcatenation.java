package com.loserico.algorithm.exam_egroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 数组拼接
 * <p/>
 * 问题描述 <p/>
 * 现在有多组整数数组，需要将它们合并成一个新的数组。合并规则如下：从每个数组里按顺序取出固定长度的内容合并到新的数组中，取完的内容会删除掉。如果该行不足固定长度或者已经为空，则直接取出剩余部分的内容放到新的数组中，继续下一行。
 * <p/>
 * 输入格式 <p/>
 * 第一行是每次读取的固定长度L，满足  <p/>
 * 第二行是整数数组的数目N，满足  <p/>
 * 第3 到第 𝑁+2 行是需要合并的数组，不同的数组用回车换行分隔，数组内部用逗号分隔，每个数组最多不超过100 个元素。
 * <p/>
 * 输出格式 <p/>
 * 输出一个新的数组，用逗号分隔。
 * <p/>
 * <p>
 * 样例输入1
 * <pre> {@code
 * 3
 * 2
 * 2,5,6,7,9,5,7
 * 1,7,4,3,4
 * }</pre>
 * <p>
 * 样例输出1
 * <pre> {@code
 * 2,5,6,1,7,4,7,9,5,3,4,7
 * }</pre>
 * <p>
 * 样例输入2
 * <pre> {@code
 * 4
 * 3
 * 1,2,3,4,5,6
 * 1,2,3
 * 1,2,3,4
 * }</pre>
 * <p>
 * 样例输出2
 * <pre> {@code
 * 1,2,3,4,1,2,3,1,2,3,4,5,6
 * }</pre>
 * <p>
 * 样例输入3
 * <pre> {@code
 * 3
 * 2
 * 2,5,6,7,,,9,5,7
 * 1,7,4,3,,4
 * }</pre>
 * <p>
 * 样例输出3
 * <pre> {@code
 * 2,5,6,1,7,4,7,9,5,3,4,7
 * }</pre>
 * <p/>
 * Copyright: Copyright (c) 2024-10-10 10:03
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ArrayConcatenation {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入每次读取的固定长度: ");
		int l = scanner.nextInt(); //第一行输入，每次读取的固定长度 L
		System.out.print("请输入数组的数量: ");
		int n = scanner.nextInt(); //数组的数量 N
		scanner.nextLine();
		// 初始化存储所有数组的列表
		List<List<Integer>> arrays = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第"+(i+1)+"个数组: ");
			String line = scanner.nextLine().trim();
			List<Integer> array = new ArrayList<>();
			for (String str : line.split(",")) {
				if (str.trim().length() > 0) {
					array.add(Integer.parseInt(str.trim()));
				}
			}
			arrays.add(array);
		}

		// 结果数组
		List<Integer> result = new ArrayList<>();

		// 是否还有元素可以继续处理
		boolean moreToProcess = false;

		do {
			moreToProcess = false;
			// 遍历每个数组
			for (List<Integer> array : arrays) {
				if (!array.isEmpty()) {
					moreToProcess = true;// 只要有一个数组非空，就继续处理
					int count = Math.min(l, array.size());// 取固定长度 L 或剩余元素较少的数量
					for (int i = 0; i < count; i++) {
						result.add(array.remove(0));// 从数组中移除元素并加入到结果中
					}
				}
			}
		} while (moreToProcess);

		// 输出结果数组
		System.out.println(result.toString().replaceAll("[\\[\\] ]", ""));
	}
}
