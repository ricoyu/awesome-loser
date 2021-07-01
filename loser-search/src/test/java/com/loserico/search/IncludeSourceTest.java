package com.loserico.search;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-06-23 17:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IncludeSourceTest {
	
	@Test
	public void testQueryIncludeSource() {
		Object result = ElasticUtils.Query
				.termQuery("event_*")
				.query("event_hash", "dcc3fa7e1f608e23857a90ab0cc2fd5608e06733dc6bdef278849c8c6deafa5b")
				.includeSources("create_time")
				.queryForOne();
		System.out.println(result);
	}
}
