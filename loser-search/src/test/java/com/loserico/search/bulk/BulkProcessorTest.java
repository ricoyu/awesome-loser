package com.loserico.search.bulk;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils;
import com.loserico.search.builder.bulk.ESBulkProcessor;
import com.loserico.search.pojo.NetLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2021-11-15 15:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class BulkProcessorTest {
	
	@SneakyThrows
	@Test
	public void testBulkProcessor() {
		ElasticUtils.ping();
		ESBulkProcessor esBulkProcessor = new ESBulkProcessor();
		BulkProcessor bulkProcessor = esBulkProcessor.bulkProcessor();
		String metadata = IOUtils.readClassPathFileAsString("metadata.json");
		Map<String, Object> doc = JacksonUtils.toMap(metadata);
		
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 30000; i++) {
			IndexRequest indexRequest = new IndexRequest("bulk_test").source(doc);
			bulkProcessor.add(indexRequest);
		}
		
		bulkProcessor.flush();
		bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
		long end = System.currentTimeMillis();
		log.info("{} 条 BulkProcess 花费 {}", 30000, (end - begin));
	}
	
	@SneakyThrows
	@Test
	public void test() {
		ElasticUtils.ping();
		ESBulkProcessor esBulkProcessor = new ESBulkProcessor();
		BulkProcessor bulkProcessor = esBulkProcessor.bulkProcessor();
		String metadata = IOUtils.readClassPathFileAsString("metadata.json");
		NetLog doc = JacksonUtils.toObject(metadata, NetLog.class);
		
		long begin = System.currentTimeMillis();
		List<NetLog> docs = new ArrayList<>();
		for (int i = 0; i <30000; i++) {
			docs.add(doc);
		}
		ElasticUtils.bulkIndex("bulk_test2", docs);
		long end = System.currentTimeMillis();
		log.info("{} 条 IndexRequest 花费 {}", 30000, (end - begin));
	}
}
