package com.loserico.search;

import com.loserico.search.enums.FieldType;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-06-06 14:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RangeQueryTest {
	
	@Test
	public void testDateRange() {
		List<Object> products = ElasticUtils.Query.range("products")
				.field("date")
				.gte("2018-01-01")
				.lte("2019-01-01")
				.queryForList();
		
		products.forEach(System.out::println);
	}
	
	@Test
	public void testDateExpression() {
		List<Object> products = ElasticUtils.Query.range("products")
				.field("date")
				.gt("now-4y")
				.queryForList();
		
		assertThat(products).size().isEqualTo(1);
		products.forEach(System.out::println);
	}
	
	@SneakyThrows
	@Test
	public void testDateMillisThenRange() {
		ElasticUtils.Admin.deleteIndex("products2");
		boolean created = ElasticUtils.Admin.createIndex("products2")
				.mapping()
				.field("price", FieldType.LONG)
				.field("avaliable", FieldType.BOOLEAN)
				.field("date", FieldType.DATE)
				.field("productID", FieldType.KEYWORD)
				.thenCreate();
		assertTrue(created);
		
		List<String> docs = asList(
				"{\"price\": 10, \"avaliable\": true, \"date\": 1557638431000, \"productID\": \"XHDK-A-1293-#fJ3\"}", //2019-05-12 13:20:31
				//"{\"price\": 20, \"avaliable\": true, \"date\": 1622967428984, \"productID\": \"KDKE-B-9947-#kL5\"}",  //2021-06-06 16:17:08
				"{\"price\": 20, \"avaliable\": true, \"date\": \"2021-06-06 16:17:08\", \"productID\": \"KDKE-B-9947-#kL5\"}",  //2021-06-06 16:17:08
				"{\"price\": 30, \"avaliable\": true, \"productID\": \"J0DL-X-1937-#pV7\"}", 
				"{\"price\": 30, \"avaliable\": true, \"productID\": \"QQPX-R-3956-#aD8\"}");
		
		int count = ElasticUtils.bulkIndex("products2", docs).getSuccessCount();
		assertThat(count).isEqualTo(4);
		TimeUnit.SECONDS.sleep(1);
		List<Object> products = ElasticUtils.Query.range("products2")
				.field("date")
				//.gte("2021-06-06 16:17:08")
				.gte("now-1d")
				.queryForList();
		products.forEach(System.out::println);
		assertThat(products).size().isEqualTo(1);
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1622967428984L));
	}
}
