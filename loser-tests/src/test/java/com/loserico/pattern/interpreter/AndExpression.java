package com.loserico.pattern.interpreter;

/**
 * <p>
 * Copyright: (C), 2020/1/29 11:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AndExpression implements Expression {
	
	private Expression expr1 = null;
	private Expression expr2 = null;
	
	public AndExpression(Expression expr1, Expression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}
	
	@Override
	public boolean interpreter(String context) {
		return expr1.interpreter(context) && expr2.interpreter(context);
	}
}
