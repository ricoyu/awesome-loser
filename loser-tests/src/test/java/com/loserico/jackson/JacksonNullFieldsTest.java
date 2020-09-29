package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-29 10:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonNullFieldsTest {
	
	@SneakyThrows
	@Test
	public void testgivenNullsIgnoredOnClass_whenWritingObjectWithNullField_thenIgnored() {
		ObjectMapper objectMapper = new ObjectMapper();
		MyDto myDto = new MyDto();
		String s = objectMapper.writeValueAsString(myDto);
		System.out.println(s);
		assertThat(s, containsString("intValue"));
		assertThat(s, not(containsString("strValue")));
	}
	
	/**
	 * 全局Ignore null 字段
	 */
	@SneakyThrows
	@Test
	public void testgivenNullsIgnoredGlobally_whenWritingObjectWithNullField_thenIgnored() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		MyDto myDto = new MyDto();
		String s = mapper.writeValueAsString(myDto);
		System.out.println(s);
		assertThat(s, containsString("intValue"));
		assertThat(s, not(containsString("strValue")));
	}
	
	@Data
	static class MyDto {
		
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String strValue;
		
		private int intValue;
	}
}
