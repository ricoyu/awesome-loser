package com.loserico.search;

import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.search.vo.ElasticPage;
import com.loserico.search.vo.ElasticScroll;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2023-08-09 14:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsPagingTest {
	
	@Test
	public void testFromSizePaging() {
		ElasticPage<String> page = ElasticUtils.Query
				.matchAllQuery("kibana_sample_data_ecommerce")
				.paging(1, 1)
				.includeSources("customer_full_name")
				.queryForPage();
		
		List<String> results = page.getResults();
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0)).isEqualTo("Mary Bailey");
		results.forEach(System.out::println);
	}
	
	@Test
	public void testSearchAfter() {
		ElasticPage<String> page = ElasticUtils.Query
				.matchAllQuery("users")
				.size(1)
				.sort("age:desc,_id:asc")
				.resultType(String.class)
				.queryForPage();
		String user4 = page.getResults().get(0);
		String username = JsonPathUtils.readNode(user4, "name");
		assertThat(username).isEqualTo("user4");
		Object[] sort = page.getSort();
		
		page = ElasticUtils.Query.matchAllQuery("users")
				.size(1)
				.sort("age:desc,_id:asc")
				.searchAfter(sort)
				.queryForPage();
		
		String user3 = page.getResults().get(0);
		username = JsonPathUtils.readNode(user3, "name");
		assertThat(username).isEqualTo("user3");
		page.getResults().forEach(System.out::println);
		
		sort = page.getSort();
		page = ElasticUtils.Query.matchAllQuery("users")
				.size(1)
				.sort("age:desc,_id:asc")
				.searchAfter(sort)
				.queryForPage();
		
		String user2 = page.getResults().get(0);
		username = JsonPathUtils.readNode(user2, "name");
		assertThat(username).isEqualTo("user2");
	}
	
	@Test
	public void testScroll() {
		ElasticScroll<String> scroll = ElasticUtils.Query
				.matchAllQuery("users")
				.size(1)
				.scroll(5, TimeUnit.MINUTES)
				.queryForScroll();
		String name = JsonPathUtils.readNode(scroll.getResults().get(0), "name");
		assertThat(name).isEqualTo("user1");
		System.out.println("=================================");
		
		scroll = ElasticUtils.Query
				.scrollQuery("users")
				.scrollId(scroll.getScrollId())
				.queryForScroll();
		name = JsonPathUtils.readNode(scroll.getResults().get(0), "name");
		assertThat(name).isEqualTo("user2");
		assertThat(scroll.getResults().size()).isEqualTo(1);
	}
}
