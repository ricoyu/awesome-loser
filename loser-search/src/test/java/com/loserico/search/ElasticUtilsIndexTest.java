package com.loserico.search;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.ElasticUtils.Admin;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;
import com.loserico.search.pojo.Movie;
import com.loserico.search.pojo.NetLog;
import com.loserico.search.support.FieldDef;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-05-06 15:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsIndexTest {
	
	
	@Test
	public void testCreateIndex() {
		boolean created = ElasticUtils.Admin.createIndex("boduo")
				.mapping(Dynamic.FALSE)
				.field("name", FieldType.TEXT)
				.field("income", FieldType.LONG).index(false)
				.field("carrer", FieldType.TEXT).index(true)
				.analyzer(Analyzer.IK_MAX_WORD)
				.searchAnalyzer(Analyzer.IK_SMART)
				.thenCreate();
		
		System.out.println(created);
	}
	
	@Test
	public void testCreateIndexByAnnotation() {
		ElasticUtils.Admin.deleteIndex("movie");
		boolean created = Admin.createIndex(Movie.class);
		assertTrue(created);
	}
	
	@Test
	public void testExistsIndex() {
		assertTrue(Admin.existsIndex("dynamic_mapping_test"));
	}
	
	@Test
	public void testListAllIndices() {
		List<String> indices = Admin.listIndices();
		indices.forEach(System.out::println);
	}
	
	@Test
	public void testPutMapping() {
		boolean acknowledged = ElasticUtils.Mappings.putMapping("rico", Dynamic.FALSE)
				.copy("movies")
				.field("title", FieldType.KEYWORD).index(true)
				.analyzer(Analyzer.ENGLISH)
				.searchAnalyzer(Analyzer.ENGLISH)
				.thenCreate();
		System.out.println(acknowledged);
	}
	
	@Test
	public void testPutMappingWithDeleteFieldDef() {
		boolean acknowledged = ElasticUtils.Mappings.putMapping("rico", Dynamic.TRUE)
				.copy("movies")
				.field("title", FieldType.KEYWORD)
				.index(true)
				.and()
				.delete("user", "genre")
				.thenCreate();
		System.out.println(acknowledged);
	}
	
	@Test
	public void testPutMappingAddNewFields() {
		ElasticUtils.Mappings.putMapping("boduo", Dynamic.TRUE)
				.field("fans", FieldType.TEXT)
				.thenCreate();
	}
	
	@Test
	public void testPutMappingWithChildField() {
		/*boolean created = ElasticUtils.createIndex("titles").create();
		boolean acknowledged = ElasticUtils.putMapping("titles", MappingBuilder.newInstance()
				.field(FieldDef.builder("title", FieldType.TEXT)
						.fields(FieldDef.builder("std", FieldType.TEXT)
								.analyzer(Analyzer.STANDARD)
								.build())
						.build()));
		System.out.println(acknowledged);*/
		
		Admin.deleteIndex("titles");
		boolean acknowledged = Admin.createIndex("titles")
				.mapping()
				.field("title", FieldType.TEXT)
				.fields(FieldDef.builder("std", FieldType.TEXT).analyzer(Analyzer.STANDARD))
				.thenCreate();
	}
	
	@Test
	public void testBulkIndexVSIndex() {
		String metadata = IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\NTA测试数据\\ids-metadata-http.json");
		List<NetLog> netLogs = new ArrayList<>();
		for (int i = 0; i < 3000; i++) {
			NetLog netLog = JacksonUtils.toObject(metadata, NetLog.class);
			netLogs.add(netLog);
		}
		
		ElasticUtils.ping();
		
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			ElasticUtils.bulkIndex("test-bulk-perf", netLogs);
		}
		long end = System.currentTimeMillis();
		System.out.println("Bulk耗费" + (end - begin));
		
		begin = System.currentTimeMillis();
		for (int i = 0; i < 30000; i++) {
			ElasticUtils.index("test-single-perf", metadata);
		}
		end = System.currentTimeMillis();
		System.out.println("Single耗费" + (end - begin));
	}
	
	@Test
	public void testBulkUpdate() {
		TransportClient client = ElasticUtils.client;
		Map<String, Object> doc = new HashMap<>();
		doc.put("src_country", "中国");
		doc.put("src_city", "苏州");
		doc.put("hobby", "苏州");
		doc.put("lovers", "苏州");
		
		
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk()
				.add(new UpdateRequest("test-single-perf", "qNgT6XwBRSj7f1nTb7Cq").doc(doc))
				.add(new UpdateRequest("test-single-perf", "qdgT6XwBRSj7f1nTb7Cv").doc("my_name", "三少爷"))
				.add(new UpdateRequest("test-bulk-perf", "ONcT6XwBRSj7f1nTCuzQ").doc(doc))
				.add(new UpdateRequest("test-bulk-perf", "PNcT6XwBRSj7f1nTCuzQ").doc(doc));
		try {
			BulkResponse responses = bulkRequestBuilder.execute().get();
			boolean hasFailures = responses.hasFailures();
			System.out.println("hasFailures " + hasFailures);
			BulkItemResponse[] items = responses.getItems();
			for (BulkItemResponse item : items) {
				boolean failed = item.isFailed();
				System.out.println("failed " + failed);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		
	}
}
