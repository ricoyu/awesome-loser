package com.loserico.search;

import com.loserico.search.annotation.DocId;
import com.loserico.search.builder.MappingBuilder;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.FieldDef;
import com.loserico.search.support.UpdateResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.cluster.metadata.IndexTemplateMetaData;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;
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
		boolean created = ElasticUtils.deleteIndex("boduo");
		System.out.println(created);
	}
	
	@Test
	public void testCreateIndex() {
		boolean created = ElasticUtils.createIndex("boduo")
				.mapping(MappingBuilder.newInstance()
						.dynamic(Dynamic.TRUE)
						.field("name", FieldType.TEXT)
						.field("income", FieldType.LONG, false)
						.field(FieldDef.builder("carrer", FieldType.TEXT)
								.index(true)
								.analyzer(Analyzer.IK_MAX_WORD)
								.searchAnalyzer(Analyzer.IK_SMART)
								.build()))
				.create();
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
	public void testBulkCreateProduct() {
		List<Product> products = asList(new Product("1", "XHDK-A-1293-#fJ3", "iPhone"),
				new Product("2", "KDKE-B-9947-#kL5", "iPad"),
				new Product("3", "JODL-X-1937-#pV7", "MBP"));
		BulkResult bulkResult = ElasticUtils.bulkIndex("products", products);
		System.out.println(toJson(bulkResult));
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Product {
		
		@DocId
		private String id;
		
		private String productId;
		
		private String desc;
		
		
	}
	
	@Test
	public void testGetById() {
		User user = ElasticUtils.get("rico", "ShNSknYBCTOc63k_prHg", User.class);
		System.out.println(user);
	}
	
	@Test
	public void testMget() {
		List<String> results = ElasticUtils.mget()
				.add("rico", "wdL8N3YBfUXxQhjl2Pc2")
				.add("rico", "vNLwN3YBfUXxQhjlgvfr")
				.add("users", asList("1", "2"))
				.request();
		results.forEach(System.out::println);
	}
	
	@Test
	public void testUpdate() {
		/*DocWriteResponse.Result result = ElasticUtils.update("users", "1", "{\n" +
				"  \"firstName\": \"Rico\",\n" +
				"  \"lastName\": \"Johnson\"\n" +
				"}");*/
		UpdateResult updateResult = ElasticUtils.update("users", "1", "{\"nickname\": \"三少爷\"}");
		System.out.println(toJson(updateResult));
	}
	
	@Test
	public void testUpsert() {
		UpdateResult updateResult = ElasticUtils.upsert("users", "3", "{\n" +
				"  \"firstName\": \"Rico\",\n" +
				"  \"lastName\": \"Johnson\"\n" +
				"}");
		System.out.println(toJson(updateResult));
	}
	
	@Test
	public void testUpsert2() {
		UpdateResult updateResult = ElasticUtils.upsert("users", "3", "{\"nickname\": \"三少爷\"}");
		System.out.println(toJson(updateResult));
	}
	
	@Test
	public void testDelteDoc() {
		boolean deleted = ElasticUtils.delete("rico", "UWHGu3YBDs-1X2rMuqw4");
		System.out.println(deleted);
	}
	
	@Test
	public void testDeleteBy() {
		long deleted = ElasticUtils.deleteBy("rico", "user", "Sea King");
		System.out.println(deleted);
	}
	
	@Test
	public void testExists() {
		boolean exists = ElasticUtils.exists("users", "3");
		System.out.println(exists);
	}
	
	@Test
	public void testGetMapping() {
		Object mapping = ElasticUtils.getMapping("boduo");
		System.out.println(toJson(mapping));
	}
	
	@Test
	public void testGetFieldMapping() {
		Map<String, Map<String, Object>> result = ElasticUtils.getMapping("boduo", "carrer", "fans", "income");
		System.out.println(toJson(result));
	}
	
	@Test
	public void testPutMapping() {
		boolean acknowledged = ElasticUtils.putMapping("rico", MappingBuilder.newInstance()
				.copy("movies")
				.dynamic(Dynamic.TRUE)
				.field(FieldDef.builder("title", FieldType.KEYWORD)
						.index(true)
						.build()));
		System.out.println(acknowledged);
	}
	
	@Test
	public void testPutMappingWithDeleteFieldDef() {
		boolean acknowledged = ElasticUtils.putMapping("rico", MappingBuilder.newInstance()
				.copy("movies")
				.dynamic(Dynamic.TRUE)
				.field(FieldDef.builder("title", FieldType.KEYWORD)
						.index(true)
						.build())
				.delete("user", "genre"));
		System.out.println(acknowledged);
	}
	
	@Test
	public void testPutMappingAddNewFields() {
		ElasticUtils.putMapping("boduo", MappingBuilder.newInstance()
				.field("fans", FieldType.TEXT));
	}
	
	@Test
	public void testPutIndexTemplate() {
		boolean created = ElasticUtils.putIndexTemplate("demo-index-template")
				.order(0)
				.patterns("test*")
				.version(0)
				.settings(Settings.builder()
						.put("number_of_shards", 1)
						.put("number_of_replicas", 1)
						.build())
				.mappings(MappingBuilder.newInstance()
						.dynamic(Dynamic.TRUE)
						.field("username", FieldType.KEYWORD)
						.field("read_books", FieldType.TEXT)
						.field(FieldDef.builder("hobbys", FieldType.TEXT).index(true).build()))
				.execute();
		System.out.println(created);
	}
	
	@Test
	public void testGetIndexTemplate() {
		IndexTemplateMetaData indexTemplateMetaData = ElasticUtils.getIndexTemplate("demo-index-template");
		System.out.println(toJson(indexTemplateMetaData));
	}
	
	@Test
	public void testCreateIndexWithIndexTemplate() {
		boolean created = ElasticUtils.createIndex("testINdex").create();
	}
	
	@Test
	public void testDeleteIndexTemplate() {
		boolean deleted = ElasticUtils.deleteIndexTemplate("demo-index-template");
		System.out.println(deleted);
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
