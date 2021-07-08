package com.loserico.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2021-07-07 11:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HashMapTest {
	
	@Test
	public void testHashMapInit() {
		Map<String, String> stringStringMap = Collections.singletonMap("rico", "三少爷");
		
		Map<String, String> myMap = new HashMap<String, String>(1) {{
			put("a", "b");
		}};
		
		System.out.println(toJson(myMap));
	}
}
