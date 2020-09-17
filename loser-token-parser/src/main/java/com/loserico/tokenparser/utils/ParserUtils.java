package com.loserico.tokenparser.utils;

import com.loserico.tokenparser.parsing.GenericTokenParser;
import com.loserico.tokenparser.parsing.OgnlTokenHandler;
import com.loserico.tokenparser.parsing.TokenHandler;

/**
 * <p>
 * Copyright: (C), 2020-09-16 14:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ParserUtils {
	
	private static final String OPEN_TOKEN = "#{";
	private static final String CLOSE_TOKEN = "}";
	
	/**
	 * 解析str中包含的OGNL表达式, 并返回解析后完整的str
	 * @param str
	 * @param root
	 * @return String
	 */
	public static String parse(String str, Object root) {
		TokenHandler tokenHandler = new OgnlTokenHandler(root);
		GenericTokenParser tokenParser = new GenericTokenParser(OPEN_TOKEN, CLOSE_TOKEN, tokenHandler);
		return tokenParser.parse(str);
	}
}
