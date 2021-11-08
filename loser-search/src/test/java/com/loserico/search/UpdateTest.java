package com.loserico.search;

import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.search.support.UpdateResult;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-07-05 16:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UpdateTest {
	
	@Test
	public void testUpdate() {
		Map<String, Object> params = new HashMap<>();
		params.put("ioc", "scan.SCAN Potential http Scan 5800-5820");
		ElasticUtils.update("event_2021-07-05", "bwHedXoBzxBQ9_x__fEj", params);
	}
	
	@Test
	public void testUpdate2() {
		Map<String, Object> params = new HashMap<>();
		params.put("severity", "medium");
		ElasticUtils.update("event_2021-07-05", "ewHedXoBzxBQ9_x__fEj", params);
	}
	
	@Test
	public void testUpdate3() {
		UpdateResult updateResult = ElasticUtils.update("event_2021-07-28")
				.id("kXH-6noBQ0ivCZkfdVFW")
				.refresh(true)
				.doc("{\"focus_status\": 1}")
				.update();
		assertEquals(updateResult.getResult(), UpdateResult.Result.UPDATED);
		String event = ElasticUtils.get("event_2021-07-28", "kXH-6noBQ0ivCZkfdVFW");
		Object value = JsonPathUtils.readNode(event, "$.focus_status");
		assertEquals( 1, value);
	}
	
	@Test
	public void test4() {
		UpdateResult updateResult = ElasticUtils.update("event_2021-07-28", "kXH-6noBQ0ivCZkfdVFW", "{\"focus_status\": 1}");
		//assertEquals(updateResult.getResult(), UpdateResult.Result.UPDATED);
		String event = ElasticUtils.get("event_2021-07-28", "kXH-6noBQ0ivCZkfdVFW");
		Object value = JsonPathUtils.readNode(event, "$.focus_status");
		assertEquals(0, value);
	}
}
