package com.loserico.algorithm;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SQLCountQueryGenerator {
	
	private static ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
	public static String generateCountQuery(String originalQuery) {
		String countSql = cache.get(originalQuery);
		if (countSql != null) {
			if(cache.size() > 1000) {
				cache.clear();
			}
			return countSql;
		}
		try {
			// 解析原始SQL查询
			Statement statement = CCJSqlParserUtil.parse(originalQuery);
			
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
					cache.put(originalQuery, countSql);
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
		String originalSqlQuery = "SELECT (SELECT column1 FROM table1 WHERE condition1) AS subquery1, \n" +
				"       (SELECT column2 FROM table2 WHERE condition2) AS subquery2 \n" +
				"FROM (SELECT column3 FROM table3 WHERE condition3) AS subquery3\n" +
				"WHERE condition4";
		//String originalSqlQuery = "SELECT column1, column2 FROM table1 WHERE condition";
		
		// 生成COUNT(*)查询
		StopWatch stopWatch = new StopWatch("countSqlQuery");
		stopWatch.start();
		String countSqlQuery = null;
		for (int i = 0; i < 10000; i++) {
			countSqlQuery = generateCountQuery(originalSqlQuery);
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
		
		// 输出生成的COUNT(*)查询
		System.out.println("COUNT(*) Query: " + countSqlQuery);
	}
}
