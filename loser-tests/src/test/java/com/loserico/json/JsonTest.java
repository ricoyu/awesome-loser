package com.loserico.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

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
	
	@Test
	public void test() {
		String json = IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\dga-metadata-response - failed.json");
		JSONObject jsonObject = JSONObject.parseObject(json);
		Optional<Object> empty = Optional.empty();
		if (empty.isPresent()) {
			System.out.println(".............");
		}
		JSONArray dnsAnswers = jsonObject.getJSONArray("dns_answers");
		Optional<String> firstIp = dnsAnswers.stream().map((obj) -> {
			JSONObject jObj = (JSONObject) obj;
			String rrtype = jObj.getString("rrtype");
			if ("A".equalsIgnoreCase(rrtype)) {
				return jObj.getString("rdata");
			}
			return null;
		}).filter(Objects::nonNull).findFirst();
		
		System.out.println(firstIp.get());
	}
}
