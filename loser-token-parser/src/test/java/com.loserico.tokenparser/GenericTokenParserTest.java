package com.loserico.tokenparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2020-09-16 11:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GenericTokenParserTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		/*String name = "三少爷";
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);*/
		/*OgnlCacheTest.User params = new OgnlCacheTest.User();
		params.setName("三少爷");
		TokenHandler tokenHandler = new OgnlTokenHandler(params);
		GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);
		
		String content = "{'name': '#{name}'}";
		String parse = tokenParser.parse(content);
		System.out.println(parse);
		
		content = "{$and: [{\"salary\": {$in: #{salarys}}}, {\"job\": \"${job}\"}]}";*/
		
		int[] salarys = new int[]{1000, 2000, 3000};
		System.out.println(salarys.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(salarys));
	}
}
