package com.loserico.common.lang.utils;

import com.loserico.common.lang.exception.SqlParseException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <p>
 * Copyright: (C), 2023-10-02 19:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SqlUtils {
	
	private static ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
	
	/**
	 * 给定任意的select语句, 生成对应的count语句
	 * @param originalQuerySql
	 * @return
	 */
	public static String generateCountSql(String originalQuerySql) {
		String countSql = cache.get(originalQuerySql);
		if (isNotEmpty(countSql)) {
			if(cache.size() > 1000) {
				cache.clear();
			}
			return countSql;
		}
		try {
			// 解析原始SQL查询
			Statement statement = CCJSqlParserUtil.parse(originalQuerySql);
			
			if (statement instanceof Select) {
				Select selectStatement = (Select) statement;
				SelectBody selectBody = selectStatement.getSelectBody();
				
				if (selectBody instanceof PlainSelect) {
					PlainSelect plainSelect = (PlainSelect) selectBody;
					
					// 构建新的COUNT(*)查询
					SelectExpressionItem countItem = new SelectExpressionItem(new Column("COUNT(*)"));
					plainSelect.setSelectItems(Arrays.asList(countItem));
					
					// 转换为字符串形式的COUNT(*)查询
					countSql = selectStatement.toString();
					cache.put(originalQuerySql, countSql);
					return countSql;
				}
			}
		} catch (JSQLParserException e) {
			throw new SqlParseException(originalQuerySql, e);
			// 处理解析错误，根据需求返回空字符串或抛出异常
		}
		
		// 如果无法正确处理原始查询，返回空字符串或抛出异常
		throw new SqlParseException(originalQuerySql);
	}
}
