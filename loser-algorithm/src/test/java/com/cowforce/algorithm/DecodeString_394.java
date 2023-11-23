package com.cowforce.algorithm;

import java.util.Stack;

/**
 * <p>
 * Copyright: (C), 2022-12-05 11:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DecodeString_394 {
	
	/**
	 * 3[a2[c]]
	 * @param s
	 * @return
	 */
	public static String decodeString(String s) {
		//3[a2[c]]
		String result = "";
		Stack<Integer> countStack = new Stack<>(); //存数字
		Stack<String> resultStack = new Stack<>();
		int index = 0;
		while (index < s.length()) {
			char cur = s.charAt(index);
			//处理数字
			if (Character.isDigit(cur)) {
				StringBuilder count = new StringBuilder();
				//读取连续的多个数字
				while (Character.isDigit(s.charAt(index))) {
					count.append(s.charAt(index++)); //count=3; 第二遍count=2
				}
				countStack.push(Integer.parseInt(count.toString()));//countStack=3, 第二遍countStaack=3,2
			} else if (cur != '[' && cur != ']') {
				/**
				 * 如果读到的不是'['或者']', 则累积起来
				 */
				result += s.charAt(index++); //result=a
			} else if (cur == '[') {
				/*
				 * 遇到[则把累积的字符串入栈
				 * 3[a2[c]]
				 */
				resultStack.push(result);//resultStack="",第二遍resultStack="", a
				result = "";
				index++;
			} else if (cur == ']') { //第一次的时候result = c
				//处理“]”, 处理相匹配的“[”之间的字符
				StringBuilder temp = new StringBuilder(resultStack.pop()); //第一次temp=c, 第二次temp=a
				int repeatTimes = countStack.pop(); //第一次repeatTimes=2, 第二次repeatTimes=3
				for (int i = 0; i < repeatTimes; i++) {
					temp.append(result);//temp=acc
				}
				result = temp.toString();// result = cc;
				index++;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(decodeString("3[a2[c]]"));
	}
}
