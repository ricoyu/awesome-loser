package com.loserico.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-09-21 14:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonObjectArray2StringArrayTest {
	
	@SneakyThrows
	@Test
	public void test() {
		String objs = IOUtils.readClassPathFileAsString("json-objects.json");
		ObjectMapper mapper = new ObjectMapper();
		String[] strs = mapper.readValue(objs, String[].class);
		for (int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}
		
		/*List<String> strings = JacksonUtils.toList(objs, String.class);
		for (String string : strings) {
			System.out.println(string);
		}*/
	}
}
