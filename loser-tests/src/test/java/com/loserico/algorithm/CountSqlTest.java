package com.loserico.algorithm;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

/**
 * <p>
 * Copyright: (C), 2023-10-02 18:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CountSqlTest {
	public static String generateCountQuery(String originalQuery) {
		try {
			// 解析原始SQL查询
			Statement statement = CCJSqlParserUtil.parse(originalQuery);
			
			if (statement instanceof Select) {
				Select selectStatement = (Select) statement;
				SelectBody selectBody = selectStatement.getSelectBody();
				
				if (selectBody instanceof PlainSelect) {
					PlainSelect plainSelect = (PlainSelect) selectBody;
					
					// 构建新的COUNT(*)查询
					PlainSelect countSelect = new PlainSelect();
					//countSelect.setSelectItems(Arrays.asList(new SelectExpressionItem(null, "COUNT(*)")));
					countSelect.setFromItem(plainSelect.getFromItem());
					countSelect.setWhere(plainSelect.getWhere());
					
					// 将新的COUNT(*)查询转换为字符串
					return countSelect.toString();
				}
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
			// 处理解析错误，根据需求返回空字符串或抛出异常
		}
		
		// 如果无法正确处理原始查询，返回空字符串或抛出异常
		return "";
	}
	
	public static void main(String[] args) {
		// 输入原始SQL查询
		String originalSqlQuery = "SELECT column1, (SELECT column2 FROM table2) FROM table1 WHERE condition";
		
		// 生成COUNT(*)查询
		String countSqlQuery = generateCountQuery(originalSqlQuery);
		
		// 输出生成的COUNT(*)查询
		System.out.println("COUNT(*) Query: " + countSqlQuery);
	}
}
