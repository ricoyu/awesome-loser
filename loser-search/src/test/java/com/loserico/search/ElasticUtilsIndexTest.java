package com.loserico.search;

import com.loserico.search.ElasticUtils.Admin;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;
import com.loserico.search.pojo.Movie;
import com.loserico.search.support.FieldDef;
import org.junit.Test;

import java.util.List;

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
}
