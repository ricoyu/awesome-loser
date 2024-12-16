package com.loserico.search;

import org.junit.Test;

import java.util.List;

public class ElasticUtilsBoolTest {

	@Test
	public void testBool() {
		List<Object> products = ElasticUtils.Query.bool("product")
				.match("name", "游戏手机")
				.should()
				.match("desc", "游戏手机")
				.queryForList();

		products.forEach(System.out::println);
	}
}
