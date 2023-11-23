package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2022-12-17 12:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AddStrings_415 {
	
	public String addStrings(String num1, String num2) {
		StringBuilder sb = new StringBuilder();
		int carry = 0;
		for (int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0; i--, j--) {
			int n1 = i < 0 ? 0 : num1.charAt(i) - '0';
			int n2 = j < 0 ? 0 : num2.charAt(j) - '0';
			int temp = (n1 + n2 + carry) % 10;
			carry = (n1 + n2 + carry) / 10;
			sb.append(temp);
		}
		if (carry != 0) {
			sb.append(carry);
		}
		return sb.reverse().toString();
	}
}
