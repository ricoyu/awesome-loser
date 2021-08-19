package com.loserico.search;

import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.utils.HttpUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-07-15 11:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PipelineTest {
	
	@Test
	public void testCreatePipeline() {
		Object result = HttpUtils.post()
				.method(HttpMethod.PUT)
				.path("_ingest/pipeline/blog_pipeline")
				.host("192.168.100.101")
				.port(9200)
				.body("")
				.request();
	}
}
