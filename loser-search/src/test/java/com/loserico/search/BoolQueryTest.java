package com.loserico.search;

import com.loserico.search.vo.ElasticPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;
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
@Slf4j
public class BoolQueryTest {
	
	@Test
	public void testBoolRange() {
		List<Object> results = ElasticUtils.Query.bool("event")
				.range("datetime").gte(1623134434000L).lte(1623134434000L).must()
				.excludeSources("http", "payload")
				.queryForList();
		
		results.forEach(System.out::println);
		assertThat(results.size()).isEqualTo(10);
	}
	
	@Test
	public void testBoolMatch() {
		List<Object> events = ElasticUtils.Query.bool("event")
				.match("alert_risk_level", "medium").should()
				.range("datetime").gte(1623134434000L).lte(1623134434000L).must()
				.term("alert_category", "web-attack").filter()
				.includeSources("datetime", "alert_risk_level", "src_ip", "dest_ip")
				.queryForList();
		
		events.forEach(System.out::println);
	}
	
	@Test
	public void testBoolPaging() {
		ElasticPage<Object> page = ElasticUtils.Query.bool("event")
				.match("src_country", "局").must()
				.includeSources("datetime", "src_ip", "_id", "src_country")
				.sort("src_ip.keyword:asc,_id:asc")
				.paging(2, 3)
				.queryForPage();
		
		log.info("Sort: {}", toPrettyJson(page.getSort()));
		
		page.getResults().forEach(System.out::println);
	}
}
