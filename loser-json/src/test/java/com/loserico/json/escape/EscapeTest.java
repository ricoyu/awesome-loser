package com.loserico.json.escape;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-07-06 10:48
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EscapeTest {
	
	@SneakyThrows
	@Test
	public void test() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getFactory().setCharacterEscapes(new CustomCharacterEscapes());
		
		ObjectWriter writer = mapper.writer();
		
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("age");
		
		User user = User.builder()
				.columns(columns)
				.name("ricoyu")
				.age(28)
				.build();
		String jsonDataObject = mapper.writeValueAsString(user);
		System.out.println(jsonDataObject);
	}
	
	@SneakyThrows
	@Test
	public void testUnQuoteFieldName() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("age");
		
		User user = User.builder()
				.columns(columns)
				.name("ricoyu")
				.age(28)
				.build();
		
		String jsonUsers = mapper.writeValueAsString(user);
		
		System.out.println(jsonUsers);
	}
	
	@Data
	@Builder
	static class User {
		
		private String name;
		
		private Integer age;
		
		private List<String> columns;
	}
}
