package com.loserico.tokenparser.parsing;

/**
 * 负责解析OGNL表达式 
 * <p>
 * Copyright: Copyright (c) 2020-09-16 11:19
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface TokenHandler {
	
	/**
	 * 解析OGNL表达式
	 * @param ognlExpression
	 * @return
	 */
	String handleToken(String ognlExpression);
}
