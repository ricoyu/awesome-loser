package com.loserico.tokenparser.parsing;

/**
 * 负责解析#{xxx}
 * <p>
 * Copyright: Copyright (c) 2020-09-16 11:14
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class GenericTokenParser {
	
	private final String openToken;
	private final String closeToken;
	private final TokenHandler handler;
	
	public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
		this.openToken = openToken;
		this.closeToken = closeToken;
		this.handler = handler;
	}
	
	public String parse(String text) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		// #{占位符中#所在位置
		int start = text.indexOf(openToken);
		//没有找到#{
		if (start == -1) {
			return text;
		}
		
		char[] src = text.toCharArray();
		int offset = 0;
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				// this open token is escaped. remove the backslash and continue.
				builder.append(src, offset, start - offset - 1).append(openToken);
				offset = start + openToken.length();
			} else {
				// found open token. let's search close token.
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				//把#{符号前面的字符串append进来先
				builder.append(src, offset, start - offset);
				//把offset设为xxx#{中{所在位置的下一个位置
				offset = start + openToken.length();
				//}结束符位置
				int end = text.indexOf(closeToken, offset);
				
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						// this close token is escaped. remove the backslash and continue.
						expression.append(src, offset, end - offset - 1).append(closeToken);
						offset = end + closeToken.length();
						end = text.indexOf(closeToken, offset);
					} else {
						expression.append(src, offset, end - offset);
						break;
					}
				}
				
				if (end == -1) {
					// close token was not found.
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					builder.append(handler.handleToken(expression.toString()));
					offset = end + closeToken.length();
				}
			}
			start = text.indexOf(openToken, offset);
		}
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}
}
