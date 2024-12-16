package com.loserico.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SQLRegex {

	@Test
	public void testMatchSelect() {
		Pattern pattern = Pattern.compile("\\bSELECT\\b[\\s\\S]*?\\bFROM\\b\\s+", Pattern.CASE_INSENSITIVE);
		String sql = """
				select * from dept
				""";
		Matcher matcher = pattern.matcher(sql);
		assertTrue(matcher.find());

		sql = """
				update dept set department_name="架构组" where id=1;
				""";
		matcher = pattern.matcher(sql);
		assertFalse(matcher.find());

		sql = """
				select id, ip, port, asset_group_id, asset_id, `type`, `version`, `domain`, supervised, public_access, last_update, create_time, category
				                                                 from asset_service where 1=1
				""";
		matcher = pattern.matcher(sql);
		assertTrue(matcher.find());

		sql = """
				select * from accDetails where id=1 lock in share mode;
				""";
		matcher = pattern.matcher(sql);
		assertTrue(matcher.find());

		sql = """
				UPDATE accDetails SET ledgerAmount = ledgerAmount + 500 WHERE id=1;
				""";
		matcher = pattern.matcher(sql);
		assertFalse(matcher.find());

		sql = """
				select (select 1 from actor where id=1) from (select * from film where id=1) der;
				""";
		matcher = pattern.matcher(sql);
		assertTrue(matcher.find());

		sql = """
				selectAllFromDept
				""";
		matcher = pattern.matcher(sql);
		assertFalse(matcher.find());
	}
}
