package com.loserico.search;

import com.loserico.search.ElasticUtilsTest.Product;
import com.loserico.search.enums.FieldType;
import com.loserico.search.enums.SortOrder;
import com.loserico.search.support.BulkResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

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
		ElasticUtils.Admin.deleteIndex("products");
		boolean created = ElasticUtils.Admin.createIndex("products")
				.mapping()
				.field("desc", FieldType.KEYWORD)
				.thenCreate();
		if (!created) {
			System.out.println("Index create failed");
		}
		
		List<Product> products = asList(new Product("1", "XHDK-A-1293-#fJ3", "iPhone"),
				new Product("2", "KDKE-B-9947-#kL5", "iPad"),
				new Product("3", "JODL-X-1937-#pV7", "MBP"));
		BulkResult bulkResult = ElasticUtils.bulkIndex("products", products);
		
		assertThat(bulkResult.getSuccessCount() == 3);
		
		List<Object> iphones = ElasticUtils.Query.termQuery("products")
				.query("desc", "iPhone")
				.queryForList();
		assertThat(iphones.size()).isEqualTo(0);
	}
	
	@Test
	public void testTermQueryIphone() {
		ElasticUtils.Admin.deleteIndex("products");
		List<String> products = asList(
				"{\"productID\": \"XHDK-A-1293-#fJ3\", \"desc\": \"iPhone\"}",
				"{\"productID\": \"KDKE-B-9947-#kL5\", \"desc\": \"iPad\"}",
				"{\"productID\": \"JODL-X-1937-#pV7\", \"desc\": \"MBP\"}");
		BulkResult bulkResult = ElasticUtils.bulkIndex("products", products);
		bulkResult.getIds().forEach(System.out::println);
		System.out.println(bulkResult.getSuccessCount());
		
		List<Object> iphones = ElasticUtils.Query.termQuery("products")
				.query("desc", "iPhone")
				.queryForList();
		assertThat(iphones.size()).isEqualTo(0);
		
		iphones = ElasticUtils.Query.termQuery("products")
				.query("desc", "iphone")
				.queryForList();
		assertThat(iphones.size()).isEqualTo(1);
	}
	
	@Test
	public void testQuery() {
		List<String> sources = ElasticUtils.Query.query("products")
				.queryBuilder(QueryBuilders.termQuery("desc", "iphone"))
				.fetchSource(true)
				.boost(1.2f)
				.paging(2, 100)
				.addSort(SortOrder.scoreSort().asc())
				.addSort(SortOrder.fieldSort("desc.keyword").desc())
				.queryForList();
		sources.forEach(System.out::println);
		
		List<Object> products = ElasticUtils.Query.termQuery("products")
				.query("desc", "iphone")
				.fetchSource(true)
				.sort("desc.keyword:desc")
				.queryForList();
	}
	
	@Test
	public void testQuery2() {
		Product product = ElasticUtils.Query.query("products")
				.queryBuilder(QueryBuilders.termQuery("productID", "jodl"))
				.type(Product.class)
				.fetchSource(true)
				.queryForOne();
		System.out.println(product);
	}
	
	@Test
	public void testMatchQuery() {
		List<String> movies = ElasticUtils.Query.query("movies")
				.queryBuilder(QueryBuilders.matchQuery("title", "Matrix reloaded")
						.minimumShouldMatch("2"))
				.queryForList();
		
		movies.forEach(System.out::println);
	}
	
	
	@Test
	public void testMatchQuery2() {
		List<String> products = ElasticUtils.Query.query("products")
				.queryBuilder(QueryBuilders.rangeQuery("date")
						.gte("now-1y"))
				.queryForList();
		products.forEach(System.out::println);
	}
	
	@Test
	public void testExists() {
		List<String> products = ElasticUtils.Query.query("products")
				.queryBuilder(QueryBuilders.existsQuery("productID")) //查询productID值非空
				.queryForList();
		products.forEach(System.out::println);
	}
}
