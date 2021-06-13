package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-06-07 18:27
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BoolQueryTest {
	
	@Test
	public void testBool() {
		List<Object> results = ElasticUtils.Query.bool("event")
				.range("datetime").gte(1623134434000L).lte(1623134434000L).must()
				.excludeSources("http", "payload")
				.queryForList();
		
		results.forEach(System.out::println);
		assertThat(results.size()).isEqualTo(10);
	}
}
