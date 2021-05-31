package com.loserico.search;

import com.loserico.search.ElasticUtils.Query;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-05-07 17:52
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsTermQueryTest {
	
	
	@Test
	public void testTermQuery() {
		List<Object> movies = Query.termQuery("movies")
				.query("title", "beautiful")
				.size(100)
				.sort("year, title.keyword:desc")
				.queryForList();
		movies.forEach(System.out::println);
	}
	
	@Test
	public void testTermQueryThenNotFound() {
		List<Object> products = Query.termQuery("products")
				.query("desc", "iPhone")
				.queryForList();
		assertThat(products.size()).isZero();
		
		products = Query.termQuery("products")
				.query("desc", "iphone")
				.queryForList();
		assertThat(products.size()).isEqualTo(1);
	}
}
