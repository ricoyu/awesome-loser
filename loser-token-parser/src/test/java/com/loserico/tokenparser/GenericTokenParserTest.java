package com.loserico.tokenparser;

import com.loserico.tokenparser.parsing.GenericTokenParser;
import com.loserico.tokenparser.parsing.OgnlTokenHandler;
import com.loserico.tokenparser.parsing.TokenHandler;
import com.loserico.tokenparser.utils.ParserUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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
	
	@Test
	public void test() {
		String name = "三少爷";
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("age", 18);
		params.put("married", null);
		TokenHandler tokenHandler = new OgnlTokenHandler(params);
		GenericTokenParser tokenParser = new GenericTokenParser("#{", "}", tokenHandler);
		
		String content = "{'name': '#{name}', 'audit': '#{age>=18?'成年人':'未成年人'}', '婚姻':'#{married?'已婚':'未婚'}'}";
		String result1 = tokenParser.parse(content);
		
		String result2 = ParserUtils.parse(content, params);
		
		assertEquals(result1, result2);
		
		System.out.println(result1);
		System.out.println(result2);
		
		/*content = "{$and: [{\"salary\": {$in: #{salarys}}}, {\"job\": \"${job}\"}]}";
		
		int[] salarys = new int[]{1000, 2000, 3000};
		System.out.println(salarys.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(salarys));*/
	}
}
