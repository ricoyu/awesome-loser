package com.loserico.search;

import com.loserico.search.vo.ElasticPage;
import org.junit.Test;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2021-07-19 17:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticPagingTest {
	
	@Test
	public void testPaging() {
		ElasticPage<Object> elasticPage = ElasticUtils.Query.termQuery("event_2021-07-08")
				.query("proto_type", "http")
				.sort("-create_time,_id")
				.includeSources("create_time")
				.queryForPage();
		elasticPage.getResults().forEach(System.out::println);
		
		Object[] sort = elasticPage.getSort();
		System.out.println(toJson(sort));
		System.out.println("-------------------------------------------------------------\n\n");
		elasticPage = ElasticUtils.Query.termQuery("event_2021-07-08")
				.query("proto_type", "http")
				.sort("-create_time,_id")
				.includeSources("create_time")
				.searchAfter(sort)
				.queryForPage();
		elasticPage.getResults().forEach(System.out::println);
	}
}
