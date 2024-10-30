package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 考勤信息
 * <p/>
 * 题目描述 <p/>
 * 公司用一个字符串来表示员工的出勤信息
 *
 * <pre> {@code
 * absent：缺勤
 * late：迟到
 * leaveearly：早退
 * present：正常上班
 * }</pre>
 * 现需根据员工出勤信息，判断本次是否能获得出勤奖，能获得出勤奖的条件如下：
 * <ul>
 *     <li/>缺勤不超过一次；
 *     <li/>没有连续的迟到/早退；
 *     <li/>任意连续7次考勤，缺勤/迟到/早退不超过3次。
 * </ul>
 * <p>
 * 输入描述 <p/>
 * 用户的考勤数据字符串
 * <p/>
 * 记录条数 >= 1；<br/>
 * 输入字符串长度 < 10000；
 * <p/>
 * 不存在非法输入；<br/>
 * 如：
 * <pre> {@code
 * 2
 *
 * present
 * present absent present present leaveearly present absent
 * }</pre>
 * <p>
 * 输出描述 <p/>
 * 根据考勤数据字符串，如果能得到考勤奖，输出”true”；否则输出”false”，
 * <p/>
 * 对于输入示例的结果应为： <p/>
 * true false
 * <p/>
 * 用例
 * <p/>
 * 输入
 *
 * <pre> {@code
 * 2
 * present
 * present present
 * }</pre>
 * <p>
 * 输出
 * <pre> {@code
 * true
 * true
 * }</pre>
 * <p>
 * 说明
 * <p>
 * 无
 * <p>
 * 输入
 * <pre> {@code
 * 2
 * present
 * present absent present present leaveearly present absent
 * }</pre>
 * <p>
 * 输出
 * <pre> {@code
 * true
 * false
 * }</pre>
 * <p>
 * 说明
 * 无
 * <p>
 * Copyright: Copyright (c) 2024-10-09 11:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AttendanceAward {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// 读取记录的数量
		System.out.print("请输入记录的数量: ");
		int numRecords = scanner.nextInt();
		// 创建一个结果数组，用于存储每个考勤记录的判断结果
		boolean[] results = new boolean[numRecords];

		scanner.nextLine(); //清空输入缓存
		for (int i = 0; i < numRecords; i++) {
			System.out.print("请输入第" + (i + 1) + "条考勤记录: ");
			// 读取每一行的考勤数据
			String record = scanner.nextLine();
			// 判断当前考勤数据是否符合获得奖励的条件
			results[i] = isEligibleForAward(record.split(" "));
		}

		for (boolean result : results) {
			System.out.println(result ? "true " : "false ");
		}
	}

	/**
	 * 判断是否符合获得出勤奖的条件
	 *
	 * @param attendances
	 * @return
	 */
	public static boolean isEligibleForAward(String[] attendances) {
		int absentCount = 0; // 缺勤次数计数器
		boolean consecutiveLateOrEarly = false; // 连续迟到或早退的标志

		for (int i = 0; i < attendances.length; i++) {
			// 计算缺勤次数
			if (attendances[i].equalsIgnoreCase("absent")) {
				absentCount++;
			}

			// 检查是否有连续的迟到或早退
			if (i > 0 &&
					((attendances[i].equals("late") ||
							attendances[i].equals("leaveearly") ||
							attendances[i-1].equals("late") ||
							attendances[i-1].equals("leaveearly")))) {
				consecutiveLateOrEarly = true;
			}
		}

		if (absentCount >1 || consecutiveLateOrEarly) {
			return false;
		}

		// 检查任意连续7次考勤中的非正常出勤次数
		for(int i = 0; i< attendances.length-7; i++) {
			int nonPresentCount = 0;
			for(int j = i; j< i+7; j++) {
				if (!attendances[j].equals("present")) {
					nonPresentCount++;
				}
			}

			// 如果任意连续7次考勤中非正常出勤超过3次，也不符合条件
			return nonPresentCount <= 3;
		}

		return true;
	}
}
