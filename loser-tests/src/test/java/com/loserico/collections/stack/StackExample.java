package com.loserico.collections.stack;

import java.util.Stack;

/**
 * 演示栈的用法
 * <p/>
 * Java中的Stack (栈) 是一种后进先出 (LIFO) 的数据结构。Java提供了一个内置的栈类, 即java.util.Stack, 它继承自Vector类。栈的主要操作包括:
 * <ul>
 *     <li/>push(E item): 将元素压入栈顶。
 *     <li/>pop(): 从栈顶弹出元素并返回。
 *     <li/>peek(): 查看栈顶元素，但不弹出。
 *     <li/>isEmpty(): 检查栈是否为空。
 * </ul>
 *
 * search(Object o): 返回对象在栈中的位置（1为栈顶，-1表示不在栈中）。
 * <p/>
 * Copyright: Copyright (c) 2024-09-25 16:37
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class StackExample {

	public static void main(String[] args) {
		Stack<String> stack = new Stack<>();
		stack.push("A");
		stack.push("B");
		stack.push("C");

		System.out.println("当前栈: " + stack);

		// 查找元素位置
		int position = stack.search("A");
		System.out.println("元素A的位置: " + position);

		position = stack.search("C");
		System.out.println("元素C的位置: " + position);

		String elem1 = stack.get(0);
		System.out.print("栈中第0个元素: " + elem1);

		// 查看栈顶元素
		String top = stack.peek();
		System.out.println("栈顶元素: " + top);
		System.out.println("当前栈: " + stack);

		// 弹出栈顶元素
		top = stack.pop();
		System.out.println("弹出栈顶元素: " + top);
		System.out.println("当前栈: " + stack);

		// 检查栈是否为空
		boolean empty = stack.isEmpty();
		System.out.println("栈是否为空: " + empty);


	}
}
