package com.loserico.search;

import com.loserico.search.builder.ElasticSettingsBuilder;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.junit.Test;

import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-01 9:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IndexTemplateTest {
	
	@Test
	public void testCreateIndexTemplate() {
		boolean created = ElasticUtils.Admin.putIndexTemplate("template_default")
				.patterns("*")
				.order(0)
				.version(1)
				.settings(1)
				.numberOfReplicas(1)
				.thenCreate();
		assertTrue(created);
	}
	
	@Test
	public void testCreateIndexTemplateTest() {
		ElasticUtils.Admin.putIndexTemplate("tempalate_test")
				.patterns("test*")
				.order(1)
				.settings(1)
				.numberOfReplicas(1)
				.and()
				.mappings()
				.sourceEnabled(true)
				.dateDetection(false)
				.numericDetection(true)
				.thenCreate();
	}
	
	@Test
	public void testGetIndexTemplate() {
		Map<String, IndexTemplateMetaData> indexTemplates = ElasticUtils.Admin.getIndexTemplate("template_default");
		indexTemplates.entrySet().forEach((entry) -> {
			System.out.println(entry.getKey());
			System.out.println(toPrettyJson(entry.getValue()));
		});
	}
	
	
	@Test
	public void testPutIndexTemplate() {
		boolean created = ElasticUtils.Admin.putIndexTemplate("demo-index-template")
				.order(0)
				.patterns("test*")
				.version(0)
				.settings(ElasticSettingsBuilder.builder()
						.numberOfShards(1)
						.numberOfReplicas(1))
				.mappings(Dynamic.TRUE)
				.field("username", FieldType.KEYWORD)
				.field("read_books", FieldType.TEXT)
				.field("hobbys", FieldType.TEXT).index(true)
				.thenCreate();
		System.out.println(created);
	}
	
	@Test
	public void test() {
		System.out.println(System.currentTimeMillis());
	}
	
	@Test
	public void testPutIndexTemplate2() {
		boolean created = ElasticUtils.Admin.putIndexTemplate("demo-index-template")
				.order(0)
				.patterns("test*")
				.version(0)
				.settings(1)
				.numberOfReplicas(1)
				.indexRoutingAllocation("my_route", "hot")
				.and()
				.mappings(Dynamic.TRUE)
				.field("username", FieldType.KEYWORD)
				.field("read_books", FieldType.TEXT)
				.field("hobbys", FieldType.TEXT).index(true)
				.thenCreate();
		System.out.println(created);
	}
}
