package com.loserico.pattern.interpreter;

/**
 * <p>
 * Copyright: (C), 2020/1/29 11:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TerminalExpression implements Expression {
	
	private String data;
	
	public TerminalExpression(String data) {
		this.data = data;
	}
	
	@Override
	public boolean interpreter(String context) {
		if (context.contains(data)) {
			return true;
		}
		return false;
	}
}
