package com.loserico.common.lang.utils;

/**
 * HTTP 相关转义工具类
 * <p>
 * Copyright: (C), 2021-02-04 17:07
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class EscapeUtils {
	
	/**
	 * Elasticsearch 的 query_string Query, 查询的内容需要转义一下
	 * 比如查 HTTP/1.1 会报错
	 * Caused by: org.apache.lucene.queryparser.classic.ParseException: Cannot parse 'http.protocol:HTTP/1.1': Lexical error at line 1, column 23.  Encountered: <EOF> after : "/1.1"
	 * 
	 * @param s
	 * @return
	 */
	public static String escape(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '/') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
