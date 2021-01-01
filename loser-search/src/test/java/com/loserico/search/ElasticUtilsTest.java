package com.loserico.search;

import com.loserico.search.annotation.DocId;
import com.loserico.search.support.BulkResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Copyright: (C), 2021-01-01 9:06
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsTest {
	
	@Test
	public void testInitialize() {
		Class<ElasticUtils> elasticUtilsClass = ElasticUtils.class;
		assertThat(ElasticUtils.client != null);
	}
	
	@Test
	public void testDeleteIndex() {
		boolean created = ElasticUtils.deleteIndex("dynamic_mapping_test");
		System.out.println(created);
	}
	
	@Test
	public void testCreateIndex() {
		boolean created = ElasticUtils.createIndex("dynamic_mapping_test");
		System.out.println(created);
	}
	
	@Test
	public void testExistsIndex() {
		assertTrue(ElasticUtils.existsIndex("dynamic_mapping_test"));
	}
	
	@Test
	public void testListAllIndices() {
		List<String> indices = ElasticUtils.listIndices();
		indices.forEach(System.out::println);
	}
	
	@Test
	public void testCreateDoc() {
		String id = ElasticUtils.index("rico", "{\"name\": \"三少爷\"}");
		System.out.println(id);
	}
	
	@Test
	public void testCreateDocWithId() {
		String id = ElasticUtils.index("rico", "{\"name\": \"三少爷\"}", "1");
		System.out.println(id);
	}
	
	@Test
	public void testCreateDocObjectType() {
		Person person = new Person();
		person.setUser("三少爷");
		person.setComment("牛仔");
		String id = ElasticUtils.index("rico", person);
		System.out.println(id);
	}
	
	@Test
	public void testCreateDocObjectTypeAndId() {
		Person person = new Person();
		person.setUser("三少爷");
		person.setComment("牛仔");
		String id = ElasticUtils.index("rico", person);
		System.out.println(id);
	}
	
	@Test
	public void testCreateDocObjectTypeAutoId() {
		Person person = new Person();
		person.setId(1);
		person.setUser("三少爷");
		person.setComment("牛仔");
		String id = ElasticUtils.index("rico", person);
		System.out.println(id);
	}
	
	@Test
	public void testBulkCreateDoc() {
		String[] docs = new String[]{"{\"name\": \"三少爷\"}", "{\"name\": \"二少爷\"}", "{\"name\": \"大少爷\"}"};
		BulkResult bulkResult = ElasticUtils.bulkIndex("rico", docs);
		System.out.println(toJson(bulkResult));
	}
	
	@Test
	public void testBulkCreate() {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person(1, "Json", "this is jason born"));
		persons.add(new Person(2, "Icon Man", "this is Stark"));
		persons.add(new Person(3, "Sea King", "this is 海王"));
		
		BulkResult bulkResult = ElasticUtils.bulkIndex("rico", persons);
		System.out.println(toJson(bulkResult));
	}
	
	@Test
	public void testGetById() {
		User user = ElasticUtils.get("rico", "ShNSknYBCTOc63k_prHg", User.class);
		System.out.println(user);
	}
	
	@Data
	public static class User {
		private String name;
		private int age;
	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Person {
		
		@DocId
		private Integer id;
		private String user;
		private String comment;
	}
}
