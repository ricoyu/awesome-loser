package com.loserico.algorithm.exam_egroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * 流浪地球计划在赤道上均匀部署了N个转向发动机，按位置顺序编号为0~N。
 * <ol>
 *     <li/>初始状态下所有的发动机都是未启动状态;
 *     <li/>发动机启动的方式分为”手动启动"和”关联启动"两种方式;
 *     <li/>如果在时刻1一个发动机被启动，下一个时刻2与之相邻的两个发动机就会被”关联启动”;
 *     <li/>如果准备启动某个发动机时，它已经被启动了，则什么都不用做;
 *     <li/>发动机0与发动机N-1是相邻的;
 * </ol>
 * 地球联合政府准备挑选某些发动机在某些时刻进行“手动启动”。当然最终所有的发动机都会被启动。 哪些发动机最晚被启动呢?
 * <ul>输入描述
 *     <li/>第一行两个数字N和E，中间有空格
 *     <li/>N代表部署发动机的总个数，E代表计划手动启动的发动机总个数
 *     <li/>接下来共E行，每行都是两个数字T和P，中间有空格
 *     <li/>T代表发动机的手动启动时刻，P代表此发动机的位置编号。
 *     <li/>0<=T<=N.0<=P<N
 * </ul>
 *
 * <ul>输出描述
 *     <li/>第一行一个数字N，以回车结束, N代表最后被启动的发动机个数
 *     <li/>第二行N个数字，中间有空格，以回车结束, 每个数字代表发动机的位置编号，从小到大排序
 * </ul>
 *
 * 示例1
 * <p/>
 * 输入
 *  <p/>
 * 8 2
 * <p/>
 * 0 2 <br/>
 * 0 6
 * <p/>
 * 输出
 * 2
 *  <br/>
 * 0 4
 * <p/>
 * 说明
 * <p/>
 * 8个发动机;
 * 时刻0启动(2,6);
 * <p/>
 * 时刻1启动(1,3.5,7)(其中1,3被2关联启动，5，7被6关联启动);
 * 时刻2启动(0,4)(其中0被1,7关联启动，4被3,5关联启动);
 * <p/>
 * 至此所有发动机都被启动，最后被启动的有2个，分别是0和4。
 *
 * <p>
 * Copyright: Copyright (c) 2024-09-08 20:00
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class WanderingEarth {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入部署发动机的总个数 手动启动的发动机总个数: ");
		String input = scanner.nextLine();
		String[] inputs = input.split(" ");
		int n = Integer.parseInt(inputs[0]); //发送机总数
		int e = Integer.parseInt(inputs[1]); //手动启动的发动机个数

		int[] activationTime = new int[n]; // 存储每个发动机的最早启动时间
		Arrays.fill(activationTime, Integer.MAX_VALUE); // 初始化为最大值

		// 读取手动启动的发动机数据
		for (int i = 0; i < e; i++) {
			System.out.print("请输入发动机的手动启动时刻和发动机的位置编号: ");
			String input2 = scanner.nextLine();
			String[] inputs2 = input2.split(" ");
			int t = Integer.parseInt(inputs2[0]); //发动机的手动启动时刻
			int p = Integer.parseInt(inputs2[1]); //发动机的编号
			activationTime[p] = Math.min(t, activationTime[p]); // 更新启动时间
		}

		// 使用队列来处理启动过程
		Queue<int[]> queue = new LinkedList<>();
		for (int i = 0; i < n; i++) {
			if (activationTime[i] != Integer.MAX_VALUE) {
				//队列中存储的是发动机编号和启动时间
				queue.offer(new int[]{i, activationTime[i]});
			}
		}

		// 处理关联启动
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int pos = current[0];
			int time = current[1];

			// 计算左右相邻的发动机
			int left = (pos - 1 + n) % n; //计算左边发动机的位置
			int right = (pos + 1) % n; //计算右边发动机的位置

			//如果在时刻1一个发动机被启动, 下一个时刻2与之相邻的两个发动机就会被”关联启动”;
			if (activationTime[left] > time +1) {
				activationTime[left] = time + 1;
				queue.offer(new int[]{left, time + 1});
			}
			if (activationTime[right] > time + 1) {
				activationTime[right] = time + 1;
				queue.offer(new int[]{right, time + 1});
			}
		}

		// 寻找最晚启动的发动机
		int maxTime = Arrays.stream(activationTime).max().getAsInt();
		List<Integer> result = new LinkedList<>();
		//找到最晚被启动的发动机编号
		for (int i = 0; i < n; i++) {
			if (activationTime[i] == maxTime) {
				result.add(i);
			}
		}

		// 输出结果
		Collections.sort(result);
		//第一行一个数字N，以回车结束, N代表最后被启动的发动机个数
		System.out.println(result.size());
		//第二行N个数字，中间有空格，以回车结束, 每个数字代表发动机的位置编号，从小到大排序
		result.forEach(p -> System.out.print(p+" "));
		System.out.println();
	}
}
