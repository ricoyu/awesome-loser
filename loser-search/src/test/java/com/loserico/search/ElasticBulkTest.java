package com.loserico.search;

import com.loserico.common.lang.enums.Gender;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.support.BulkResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-08-31 14:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticBulkTest {
	
	@Test
	public void testBulkInsertWithEmptyData() {
		//BulkResult bulkResult = ElasticUtils.bulkIndex("test_index-001", asList());
		//assertThat(bulkResult.getSuccessCount()).isEqualTo(0);
	}
	
	@Test
	public void testBulkIndex() {
		try {
			ElasticUtils.Admin.deleteIndex("employees1");
		} catch (Exception e) {
			log.error("", e);
		}
		BulkResult bulkResult = ElasticUtils.bulkIndex("employees1")
				.docs("{ \"name\" : \"Emma\",\"age\":32,\"job\":\"Product Manager\",\"gender\":\"female\",\"salary\":35000 }",
						"{ \"name\" : \"Underwood\",\"age\":41,\"job\":\"Dev Manager\",\"gender\":\"male\",\"salary\": 50000}",
						"{ \"name\" : \"Tran\",\"age\":25,\"job\":\"Web Designer\",\"gender\":\"male\",\"salary\":18000 }")
				.refresh(true)
				.execute();
		assertThat(bulkResult.getSuccessCount()).isEqualTo(3);
		
		List<Object> employees1 = ElasticUtils.Query.matchAllQuery("employees1").queryForList();
		assertThat(employees1.size()).isEqualTo(3);
	}
	
	@Test
	public void testBulkIndex2() {
		try {
			ElasticUtils.Admin.deleteIndex("employees2");
		} catch (Exception e) {
			log.error("", e);
		}
		List<Employee> employees = asList("{ \"name\" : \"Emma\",\"age\":32,\"job\":\"Product Manager\",\"gender\":\"female\",\"salary\":35000 }",
				"{ \"name\" : \"Underwood\",\"age\":41,\"job\":\"Dev Manager\",\"gender\":\"male\",\"salary\": 50000}",
				"{ \"name\" : \"Tran\",\"age\":25,\"job\":\"Web Designer\",\"gender\":\"male\",\"salary\":18000 }")
				.stream()
				.map((json) -> JacksonUtils.toObject(json, Employee.class))
				.collect(toList());
		BulkResult bulkResult = ElasticUtils.bulkIndex("employees2")
				.docs(employees)
				.refresh(true)
				.execute();
		assertThat(bulkResult.getSuccessCount()).isEqualTo(3);
		
		List<Object> employees1 = ElasticUtils.Query.matchAllQuery("employees2").queryForList();
		assertThat(employees1.size()).isEqualTo(3);
	}
	
	@Data
	private static class Employee {
		private String name;
		
		private Integer age;
		
		private String job;
		
		private Gender gender;
		
		private BigDecimal salary;
	}
}
