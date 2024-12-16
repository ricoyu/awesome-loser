package com.loserico.algorithm.exam_egroup;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * 最小的调整次数
 * <p/>
 * 问题描述 <p/>
 * 有一个特异性的双端队列，该队列可以从头部或尾部添加数据，但是只能从头部移出数据。
 * <p/>
 * K小姐 依次执行 2n 个指令往队列中添加数据和移出数据。其中n 个指令是添加数据（可能从头部添加、也可能从尾部添加），依次添加 1 到 n； n 个指令是移出数据。
 * <p/>
 * 现在要求移除数据的顺序为 1 n。
 * <p/>
 * 为了满足最后输出的要求，K小姐可以在任何时候调整队列中数据的顺序。
 * <p/>
 * 请问K小姐 最少需要调整几次才能够满足移除数据的顺序正好是1 到 n。
 * <p/>
 * 输入格式 <p/>
 * 第一行一个整数n，表示数据的范围。
 * <p/>
 * 接下来的2n 行，其中有n 行为添加数据，指令为：
 * <p/>
 * "head add x" 表示从头部添加数据 <br/>
 * "tail add x" 表示从尾部添加数据
 * <p/>
 * 另外n 行为移出数据指令，指令为 "remove" 的形式，表示移出 1 个数据。
 * <p/>
 * 输出格式 <p/>
 * 输出一个整数，表示K小姐要调整的最小次数。
 * <p/>
 * 样例输入
 * <pre> {@code
 * 5
 * head add 1
 * tail add 2
 * remove
 * head add 3
 * tail add 4
 * head add 5
 * remove
 * remove
 * remove
 * remove
 * }</pre>
 * <p>
 * 样例输出
 * 1
 * <p/>
 * Copyright: Copyright (c) 2024-10-12 10:56
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ReorderDeque {

	public static void main(String[] args) {
		System.out.print("请输入数据范围: ");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();

		String[] commands = new String[2*n];
		for (int i = 0; i < 2*n; i++) {
			System.out.print("请输入第" + (i + 1) + "个指令: ");
			commands[i] = scanner.nextLine().trim();
		}

		System.out.println(minAdjustments(n, commands));
	}

	public static int minAdjustments(int n, String[] commands) {
		LinkedList<Integer> deque = new LinkedList<>();
		Stack<Integer> tempStack = new Stack<>();
		int target = 1;
		int adjustments = 0;

		for (String command : commands) {
			String[] parts = command.split(" ");
			String type = parts[0];

			switch (type) {
				case "head" -> {
					int x = Integer.parseInt(parts[2]);
					deque.addFirst(x);
				}
				case "tail" -> {
					int x = Integer.parseInt(parts[2]);
					deque.addLast(x);
				}
				case "remove" -> {
					while (!deque.isEmpty() && deque.peek() != target) {
						tempStack.push(deque.removeFirst());
						adjustments++;
					}
					if (!deque.isEmpty()) {
						Integer elem = deque.removeFirst();
						System.out.println("移除: " + elem);
					}
					target++;
					while (!tempStack.isEmpty()) {
						deque.addFirst(tempStack.pop());
					}
				}
			}
		}
		return adjustments;
	}
}
