package com.cowforce.algorithm;

import java.util.Deque;
import java.util.LinkedList;

/**
 * <p>
 * Copyright: (C), 2022-12-17 12:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ValidParentness_20 {
	
	public boolean isValid(String s) {
		Deque<Character> stack = new LinkedList<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(') {
				stack.push(c);
			} else if (c == '[') {
				stack.push(c);
			} else if (c == '{') {
				stack.push(c);
			}else if (c == ')') {
				if (stack.isEmpty() || stack.pop() != '(') {
					return false;
				}
			}else if(c == ']') {
				if (stack.isEmpty() || stack.pop() != '[') {
					return false;
				}
			}else if (c =='}') {
				if (stack.isEmpty() || stack.pop() !='{') {
					return false;
				}
			}
		}
		return stack.isEmpty();
	}
}
