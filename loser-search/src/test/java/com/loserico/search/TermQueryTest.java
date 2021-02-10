package com.loserico.search;

import com.loserico.search.ElasticUtilsTest.Product;
import com.loserico.search.builder.MappingBuilder;
import com.loserico.search.enums.FieldType;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.FieldDef;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2021-01-09 9:22
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TermQueryTest {
	
	@Test
	public void testTermQuery() {
		ElasticUtils.deleteIndex("products");
		boolean created = ElasticUtils.createIndex("products")
				.mapping(MappingBuilder.newInstance()
						.field(FieldDef.builder("desc", FieldType.KEYWORD)
								.build()))
				.create();
		if (!created) {
			System.out.println("Index create failed");
		}
		
		List<Product> products = asList(new Product("1", "XHDK-A-1293-#fJ3", "iPhone"),
				new Product("2", "KDKE-B-9947-#kL5", "iPad"),
				new Product("3", "JODL-X-1937-#pV7", "MBP"));
		BulkResult bulkResult = ElasticUtils.bulkIndex("products", products);
		
		assertThat(bulkResult.getSuccessCount() == 3);
	}
	
	@Test
	public void testQuery() {
		List<String> sources = ElasticUtils.query("products")
				.queryBuilder(QueryBuilders.termQuery("desc", "iphone"))
				.fetchSource(true)
				.boost(1.2f)
				.paging(2, 100)
				.addSort(SortOrder.scoreSort().asc())
				.addSort(SortOrder.fieldSort("desc.keyword").desc())
				.queryForList();
		sources.forEach(System.out::println);
	}
	
	@Test
	public void testQuery2() {
		Product product = ElasticUtils.query("products")
				.queryBuilder(QueryBuilders.termQuery("productID", "jodl"))
				.type(Product.class)
				.fetchSource(true)
				.queryForOne();
		System.out.println(product);
	}
	
	@Test
	public void testMatchQuery() {
		List<String> movies = ElasticUtils.query("movies")
				.queryBuilder(QueryBuilders.matchQuery("title", "Matrix reloaded")
				.minimumShouldMatch("2"))
				.queryForList();
		
		movies.forEach(System.out::println);
	}
	
	
	@Test
	public void testMatchQuery2() {
		List<String> products = ElasticUtils.query("products")
				.queryBuilder(QueryBuilders.rangeQuery("date")
				.gte("now-1y"))
				.queryForList();
		products.forEach(System.out::println);
	}
	
	@Test
	public void testExists() {
		List<String> products = ElasticUtils.query("products")
				.queryBuilder(QueryBuilders.existsQuery("productID")) //查询productID值非空
				.queryForList();
		products.forEach(System.out::println);
	}
}
