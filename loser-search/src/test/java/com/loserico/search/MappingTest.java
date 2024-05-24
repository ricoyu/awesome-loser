package com.loserico.search;

import com.loserico.search.builder.admin.Fields;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toPrettyJson;
import static com.loserico.search.enums.FieldType.KEYWORD;
import static com.loserico.search.enums.FieldType.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2021-05-30 21:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MappingTest {
	
	@Test
	public void testDynamicMapping() {
		ElasticUtils.Admin.deleteIndex("mapping_test");
		String id = ElasticUtils.index("mapping_test", "{\"uid\": \"123\", \"isVip\": false, \"isAdmin\": true, \"age\": 19, \"height\": 180 }", 1);
		log.info("Doc id {}", id);
		
		Map<String, Object> mapping = ElasticUtils.Mappings.getMapping("mapping_test");
		System.out.println(toPrettyJson(mapping));
	}
	
	@Test
	public void testPutMapping() {
		boolean exists = ElasticUtils.Admin.existsIndex("users");
		if (exists) {
			ElasticUtils.Admin.deleteIndex("users");
		}
		ElasticUtils.Admin.createIndex("users").create();
		boolean created = ElasticUtils.Mappings.putMapping("users", Dynamic.TRUE)
				.field("mobile", KEYWORD).index(false).nullValue("")
				//.and()
				.field("firstName", TEXT)
				//.and()
				.field("lastName", TEXT)
				.thenCreate();
		assertTrue(created);
	}
	
	@Test
	public void testGetMapping() {
		Map<String, Object> mapping = ElasticUtils.Mappings.getMapping("users");
		System.out.println(toPrettyJson(mapping));
	}
	
	@Test
	public void testCopyTo() {
		boolean exists = ElasticUtils.Admin.existsIndex("users");
		if (exists) {
			ElasticUtils.Admin.deleteIndex("users");
		}
		ElasticUtils.Admin.createIndex("users").create();
		boolean created = ElasticUtils.Mappings.putMapping("users", Dynamic.TRUE)
				.field("firstName", TEXT).copyTo("fullname")
				//.and()
				.field("lastName", TEXT).copyTo("fullname")
				.thenCreate();
		assertTrue(created);
		String id = ElasticUtils.index("users", "{\"firstName\": \"Yu\", \"lastName\": \"Xuehua\"}", 1);
		List<Object> users = ElasticUtils.Query.uriQuery("users")
				.query("fullname:(Yu Xuehua)")
				.queryForList();
		assertThat(users).size().isEqualTo(1);
		users.forEach(System.out::println);
	}
	
	@Test
	public void testArray() {
		boolean exists = ElasticUtils.Admin.existsIndex("users");
		if (exists) {
			ElasticUtils.Admin.deleteIndex("users");
		}
		ElasticUtils.Admin.createIndex("users").create();
		ElasticUtils.index("users", "{\"name\": \"onebird\", \"interests\": \"reading\"}", 1);
		ElasticUtils.index("users", "{\"name\": \"twobirds\", \"interests\": [\"reading\", \"music\"] }", 2);
		
		String user = ElasticUtils.Query.byId("users", 1);
		System.out.println(user);
	}

	@Test
	public void testCreateIndexWithExplictMapping() {
		boolean create = ElasticUtils.Admin.createIndex("my-index-000001")
				.mapping()
				.field("age", FieldType.INTEGER)
				.field("email", KEYWORD)
				.field("name", TEXT)
				.thenCreate();
		assertTrue(create);
		Map<String, Object> mapping = ElasticUtils.Mappings.getMapping("my-index-000001");
		System.out.println(toPrettyJson(mapping));
	}

	@Test
	public void testAddfield2ExistMapping() {
		boolean created = ElasticUtils.Mappings.putMapping("my-index-000001", Dynamic.TRUE)
				.field(Fields.field("employee-id", KEYWORD)
						.index(false))
				.thenCreate();

		assertTrue(created);
		Map<String, Object> mapping = ElasticUtils.Mappings.getMapping("my-index-000001");
		System.out.println(toPrettyJson(mapping));
	}
}
