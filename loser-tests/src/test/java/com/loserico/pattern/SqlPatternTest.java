package com.loserico.pattern;

import java.util.regex.Pattern;

public class SqlPatternTest {
	private static final Pattern SELECT_PATTERN =
			Pattern.compile("\\bSELECT\\b[\\s\\S]*?\\bFROM\\b\\s+", Pattern.CASE_INSENSITIVE);

	//用于判断是否insert语句
	private static final Pattern DELETE_PATTERN =
			Pattern.compile("\\bdelete\\b[\\s\\S]*?\\bFROM\\b\\s+", Pattern.CASE_INSENSITIVE);

	//用于判断是否insert语句
	private static final Pattern INSERT_PATTERN =
			Pattern.compile("\\binsert\\b[\\s\\S]*?\\binto\\b\\s+", Pattern.CASE_INSENSITIVE);

	//用于判断是否update语句
	private static final Pattern UPDATE_PATTERN =
			Pattern.compile("\\bupdate\\b[\\s\\S]*?\\bset\\b\\s+", Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {

		String sql = """
				SELECT name, age FROM users;
				""";
		isSQLStatement(sql);

		sql = """
				SELECT username, (SELECT COUNT(*) FROM orders WHERE orders.user_id = users.id) AS order_count FROM users;
				""";

		isSQLStatement(sql);

		sql = """
				SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE product_id = 5);
				""";
		isSQLStatement(sql);

		sql = """
				UPDATE users SET name = 'John' WHERE id = 1;
				""";
		isSQLStatement(sql);

		sql= """
				This is a text containing the word select but not an SQL command.
				""";
		isSQLStatement(sql);
	}

	private static void isSQLStatement(String sql) {
		if (SELECT_PATTERN.matcher(sql).find()) {
			System.out.println("select");
		} else if (DELETE_PATTERN.matcher(sql).find()) {
			System.out.println("delete");
		} else if (INSERT_PATTERN.matcher(sql).find()) {
			System.out.println("insert");
		} else if (UPDATE_PATTERN.matcher(sql).find()) {
			System.out.println("update");
		}
	}
}
