package com.loserico.algorithm.exam_egroup;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 题目描述
 * <p/>
 * 小明来到某学校当老师，需要将学生按考试总分或单科分数进行排名，你能帮帮他吗？
 * <p/>
 * 输入描述:
 * <p/>
 * 第 1 行输入两个整数，学生人数 n 和科目数量 m。
 * <p/>
 * 0 < n < 100, 0 < m < 10
 * <p/>
 * 第 2 行输入 m 个科目名称，彼此之间用空格隔开。
 * <p/>
 * 科目名称只由英文字母构成，单个长度不超过10个字符。
 * <p/>
 * 科目的出现顺序和后续输入的学生成绩一一对应。
 * <p/>
 * 不会出现重复的科目名称。
 * <p/>
 * 第 3 行开始的 n 行，每行包含一个学生的姓名和该生 m 个科目的成绩（空格隔开）
 * <p/>
 * 学生不会重名。
 * <p/>
 * 学生姓名只由英文字母构成，长度不超过10个字符。
 * <p/>
 * 成绩是0~100的整数，依次对应第2行种输入的科目。
 * <p/>
 * 第n+2行输入用作排名的科目名称。若科目不存在，则按总分进行排序。
 * <p/>
 * 输出描述:
 * <p/>
 * 输出一行: 按成绩排序后的学生名字，空格隔开。成绩相同的按照学生姓名字典顺序排序。
 *
 * <ul>用例1
 *     <li/>3 2
 *     <li/>yuwen shuxue
 *     <li/>fangfang 95 90
 *     <li/>xiaohua 88 95
 *     <li/>minmin 100 82
 *     <li/>shuxue
 * </ul>
 *
 * 输出: xiaohua fangfang minmin
 * <p/>
 * 说明: 按shuxue成绩排名，依次是xiaohua、fangfang、minmin
 *
 * <ul>用例2:
 *     <li/>3 2
 *     <li/>fangfang 95 90
 *     <li/>xiaohua 88 95
 *     <li/>minmin 90 95
 *     <li/>zongfen
 * </ul>
 *
 * 输出: fangfang minmin xiaohua
 *
 * 说明: 排序科目不存在，按总分排序，fangfang和minmin总分相同，按姓名的字典顺序，fangfang排在前面。
 *
 * <p/>
 * Copyright: Copyright (c) 2024-09-13 20:25
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StudentRanking {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		//读取学生人数和科目数量
		System.out.print("请输入读取学生人数和科目数量: ");
		String input = scanner.nextLine();
		String[] parts = input.split(" ");
		int n = Integer.parseInt(parts[0]); //学生数
		int m = Integer.parseInt(parts[1]); //科目数量

		System.out.print("请输入科目: ");
		String[] subjects = scanner.nextLine().trim().split(" ");
		//存储学生信息，姓名及其各科成绩
		List<Student> students = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			System.out.print("请输入第"+(i+1)+"个学生的姓名及各科成绩: ");
			String[] inputs = scanner.nextLine().trim().split(" ");
			String name = inputs[0].trim();
			int[] scores = new int[m];
			for (int j = 0; j < m; j++) {
				scores[j] = Integer.parseInt(inputs[j+1].trim());
			}

			students.add(new Student(name, scores));
		}

		// 读取用于排序的科目名称
		System.out.print("请输入用于排序的科目名称: ");
		String sortSubject = scanner.nextLine().trim();

		//查找排序科目的索引
		int subjectIndex = -1;
		for (int i = 0; i < m; i++) {
			if (subjects[i].equals(sortSubject)) {
				subjectIndex = i;
				break;
			}
		}

		// 根据指定科目或总分进行排序
		final int sortIndex = subjectIndex;
		Collections.sort(students, (student1, student2) -> {
			int result;
			if (sortIndex == -1) {// 总分排序
				result = Integer.compare(student2.getTotalScore(), student1.getTotalScore());
			} else {// 特定科目排序
				result = Integer.compare(student2.getScores()[sortIndex], student1.getScores()[sortIndex]);
			}

			if (result == 0) {
				result = student1.getName().compareTo(student2.getName());
			}
			return result;
		});

		//输出排序后的学生姓名
		students.forEach(student -> System.out.print(student.getName() + " "));
	}

	@Data
	static class Student {
		private String name;
		private int[] scores;

		public Student(String name, int[] scores) {
			this.name = name;
			this.scores = scores;
		}

		public int getTotalScore() {
			return Arrays.stream(scores).sum();
		}
	}
}
