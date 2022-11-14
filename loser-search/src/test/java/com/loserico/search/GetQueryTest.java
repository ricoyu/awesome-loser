package com.loserico.search;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-06-09 15:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GetQueryTest {
	
	@Test
	public void testGetById() {
		String shakespeare = ElasticUtils.Query.byId("shakespeare", "1");
		System.out.println(shakespeare);
	}
}
