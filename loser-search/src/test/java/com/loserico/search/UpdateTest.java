package com.loserico.search;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
}
