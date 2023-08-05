package com.loserico.search;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-08-01 11:03
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsExistsTest {
	
	@Test
	public void testExists() {
		List<Object> products = ElasticUtils.Query.exists("products")
				.field("date")
				.constantScore(true)
				.queryForList();
		Assertions.assertThat(products.size()==2);
	}
}
