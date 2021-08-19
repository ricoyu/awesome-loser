package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-07-22 16:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexNotExistsTest {
	
	@Test
	public void testWhenOneOfTheIndexNotExists() {
		List<Object> results = ElasticUtils.Query.termQuery("netlog_2021-07-16", "netlog_2021-07-17", "netlog_2021-07-18", "netlog_2021-07-19")
				.query("dev_id", "1")
				.queryForList();
		
		assertThat(results).isNotEmpty();
		results.forEach(System.out::println);
	}
}
