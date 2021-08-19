package com.loserico.search;

import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-08-16 13:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeleteTest {
	
	@Test
	public void testDeleteByQuery() {
		List<Object> objects = ElasticUtils.Query.termsQuery("event_2021-08-11")
				.query("attack_direction", "in_in", "in_out", "out_in")
				.queryForList();
		
		objects.forEach(System.out::println);
		
		long delete = ElasticUtils.Query.termsQuery("event_*")
				.query("attack_direction", "in_in", "in_out", "out_in")
				.delete();
		System.out.println(delete);
	}
}
