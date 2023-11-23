package com.cowforce.algorithm;

import java.util.Stack;

/**
 * <p>
 * Copyright: (C), 2022-12-04 11:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MyQueue_232 {
	
	private Stack<Integer> inStack = new Stack<>();
	private Stack<Integer> outStack = new Stack<>();
	
	public MyQueue_232() {
		
	}
	
	public void push(int x) {
		inStack.push(x);
	}
	
	public int pop() {
		if (outStack.empty()) {
			in2out();
		}
		return outStack.pop();
	}
	
	public int peek() {
		if (outStack.empty()) {
			in2out();
		}
		return outStack.peek();
	}
	
	public boolean empty() {
		return inStack.empty() && outStack.isEmpty();
	}
	
	public void in2out() {
		while (!inStack.isEmpty()) {
			outStack.push(inStack.pop());
		}
	}
}
