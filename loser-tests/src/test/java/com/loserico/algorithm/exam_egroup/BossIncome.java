package com.loserico.algorithm.exam_egroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 一个XX产品行销总公司只有一个 boss，其下有若干一级分销，一级分销又有若干二级分销，每个分销只有唯一的上级分销。
 * 规定，每个月，下级分销需要将自己的总收入（自己的+下级上交的）每满 100 元上交 15 元给自己的上级。
 * 现给出一组分销的关系和每个分销的收入，请找出 boss 并计算出这个 boss 的收入。
 * <ol>比如：
 *     <li/>收入 100 元，上交 15 元；
 *     <li/>收入 199 元（99 元不够 100），上交 15 元；
 *     <li/>收入 200 元，上交 30 元。
 * </ol>
 * <p>
 * 输入格式:
 * 第一行输入一个整数N，表示关系的总数量。
 * 接下来N 行，每行输入三个整数, 分别表示分销 ID、上级分销 ID 和收入。
 * <p>
 * 输出格式:
 * 输出两个整数，分别表示 boss 的 ID 和总收入，用空格分隔。
 * <p>
 * 样例输入:
 * 5
 * 1 0 100
 * 2 0 199
 * 3 0 200
 * 4 0 200
 * 5 0 200
 *
 * 样例输出:
 * 0 120
 * <p>
 * Copyright: Copyright (c) 2024-09-05 17:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BossIncome {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入分销关系总数：");
		int totalCount = scanner.nextInt();
		scanner.nextLine();
		Map<Integer, Integer> incomeMap = new HashMap<>(); //记录每个分销商ID的收入
		Map<Integer, Integer> parentIdMap = new HashMap<>(); //记录每个分销商ID的上级分销商ID

		for (int i = 0; i < totalCount; i++) {
			System.out.print("请输入第" + (i + 1) + "组数据：");
			String data = scanner.nextLine();
			String[] arr = data.split(" ");

			int id = Integer.parseInt(arr[0]);
			int parentId = Integer.parseInt(arr[1]);
			int income = Integer.parseInt(arr[2]);

			incomeMap.put(id, income);
			parentIdMap.put(id, parentId);
		}

		scanner.close();

		// 计算每个分销商应该上交的金额并更新老板收入
		Map<Integer, Integer> bossIncomeMap = new HashMap<>();
		for(Map.Entry<Integer, Integer> entry : incomeMap.entrySet()) {
			int id = entry.getKey();
			int income = entry.getValue();
			int contribution = income / 100 * 15;

			// 找到上级ID并累加上交的金额
			Integer parentId = parentIdMap.get(id);
			while (parentId != null) {
				bossIncomeMap.put(parentId, bossIncomeMap.getOrDefault(parentId, 0) + contribution);
				parentId = parentIdMap.get(parentId);
			}
		}

		// 找到boss（无上级的ID）
		int bossId = 0;
		int bossIncome = 0;
		for(int id : parentIdMap.values()) {
			if (!parentIdMap.containsKey(id)) {// 这个ID没有上级，即为boss
				bossId = id;
				bossIncome = bossIncomeMap.getOrDefault(bossId, 0);
				break;
			}
		}

		System.out.println(bossId + " " + bossIncome);
	}
}
