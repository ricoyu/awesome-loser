package com.loserico.search;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-09-03 17:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IdsQueryTest {
	
	@Test
	public void testIdsQuery() {
		List<Map> results = ElasticUtils.Query.idsQuery("dga_event_2021-08-30")
				.ids("-3KMqXsBnQfP0ODXFLu4", "_XKNqXsBnQfP0ODXsrtx")
				.sort("-create_time,_id")
				.resultType(Map.class)
				.queryForList();
		
		results.forEach(System.out::println);
	}
	
	@Test
	public void test() {
		System.out.println(new Date(1630665922303L));
		System.out.println(new Date().getTime());
	}
}
