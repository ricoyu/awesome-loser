package com.loserico.search;

import com.loserico.search.builder.agg.ElasticMultiTermsAggregationBuilder;
import com.loserico.search.builder.agg.sub.SubAggregations;
import com.loserico.search.vo.ElasticPage;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-09-24 11:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MultiTermsTest {
	
	@Test
	public void testMultiTermsPaging() {
		ElasticRangeQueryBuilder queryBuilder = ElasticUtils.Query.range("netlog_2021-09*")
				.field("create_time")
				.gte(1632418220000L)
				.lte(1632454220000L);
		ElasticMultiTermsAggregationBuilder aggregationBuilder = ElasticUtils.Aggs
				.multiTerms("netlog_2021-09*")
				.setQuery(queryBuilder)
				.of("multi-field", new String[]{"src_ip", "dst_ip"})
				.fetchTotalHits(true);
		
		aggregationBuilder.subAggregation(
				SubAggregations.bucketSort("multi_terms_sort")
						.paging(0, 20))
				.getPage();
		
		ElasticPage page = aggregationBuilder.getPage();
		System.out.println(page.getTotalCount());
		System.out.println(page.getTotalPages());
		System.out.println(page.getPageSize());
		System.out.println(page.getCurrentPage());
	}
}
