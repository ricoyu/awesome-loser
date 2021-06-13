package com.loserico.search;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.search.support.BulkResult;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-06-11 17:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PerformanceTest {
	
	@Test
	public void testBulkPerformance() {
		String bulkJson = IOUtils.readClassPathFileAsString("json_path_test_data_event.json");
		long begin = System.currentTimeMillis();
		List<String> sources = JsonPathUtils.readListNode(bulkJson, "$.hits.hits[*]._source");
		long end = System.currentTimeMillis();
		System.out.println("Took: " + (end - begin));
		System.out.println(sources.size());
		System.out.println(sources.get(0));
		
		BulkResult bulkResult = ElasticUtils.bulkIndex("event", sources);
		System.out.println(bulkResult.getSuccessCount());
		assertThat(bulkResult.getSuccessCount()).isEqualTo(1000);
	}
}
