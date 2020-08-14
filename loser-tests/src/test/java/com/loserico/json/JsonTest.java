package com.loserico.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-7-23 0023 14:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonTest {
	
	@Test
	public void testJsonObject() {
		try {
			throw new Exception("");
		} catch (Exception e) {
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(e));
			Integer errorCode = jsonObject.getInteger("errorCode");
			System.out.println(errorCode);
		}
	}
}
