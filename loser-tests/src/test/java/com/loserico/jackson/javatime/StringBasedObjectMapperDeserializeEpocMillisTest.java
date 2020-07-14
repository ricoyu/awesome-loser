package com.loserico.jackson.javatime;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020/5/7 19:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StringBasedObjectMapperDeserializeEpocMillisTest {
	
	@Test
	public void test() {
		String json = IOUtils.readClassPathFileAsString("check_token_response.json");
		TokenInfo tokenInfo = JacksonUtils.toObject(json, TokenInfo.class);
		System.out.println(tokenInfo.getExp());
	}
}
