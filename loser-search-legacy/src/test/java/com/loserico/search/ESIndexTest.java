package com.loserico.search;

import com.loserico.networking.utils.HttpClientUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-11-30 8:57
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ESIndexTest {
	
	/**
	 * 查看indices
	 */
	@Test
	public void testCat() {
		String response = HttpClientUtils.httpGetRequest("http://192.168.100.104:9200/_cat/indices/kibana*?v&s=index");
		System.out.println(response);
	}
}
