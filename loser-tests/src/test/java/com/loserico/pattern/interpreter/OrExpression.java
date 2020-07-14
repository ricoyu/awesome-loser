package com.loserico.pattern.interpreter;

/**
 * <p>
 * Copyright: (C), 2020/1/29 11:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OrExpression implements Expression {
	
	private Expression expression1;
	private Expression expression2;
	
	public OrExpression(Expression exp1, Expression exp2) {
		this.expression1 = exp1;
		this.expression2 = exp2;
	}
	
	@Override
	public boolean interpreter(String context) {
		return expression1.interpreter(context) || expression2.interpreter(context);
	}
}
