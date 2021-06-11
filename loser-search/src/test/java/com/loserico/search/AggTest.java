package com.loserico.search;

import com.loserico.search.vo.AggResult;
import org.junit.Test;

import java.util.List;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;

/**
 * 聚合分析
 * <p>
 * Copyright: (C), 2021-06-03 9:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AggTest {
	
	@Test
	public void testAggOnDestContry() {
		List<AggResult> aggResults = ElasticUtils.Aggs.terms("kibana_sample_data_flights")
				.name("flight_dest")
				.field("DestCountry")
				.get();
		
		aggResults.forEach(agg -> System.out.println(toPrettyJson(agg)));
	}
	
	@Test
	public void testSubAggregate() {
		ElasticUtils.Aggs.terms("kibana_sample_data_flights")
				.field("DestCountry")
				.name("flight_dest");
	}
}
