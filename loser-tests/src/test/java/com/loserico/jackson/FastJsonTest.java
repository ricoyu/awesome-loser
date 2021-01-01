package com.loserico.jackson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.fastjson.Event;
import org.junit.Test;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class FastJsonTest {
	
	@Test
	public void testWriteNull() {
		//String json = IOUtils.readFile("target\\test-classes\\fastjson.json");
		//Customer customer = JSON.parseObject(json, Customer.class);
		//System.out.println(JSON.toJSONString(customer, WRITE_MAP_NULL_FEATURES));
		System.out.println(new Date().getTime());
	}
	
	@Test
	public void testDeserialize() {
		Event event = JSONObject.parseObject(IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\ids-alert-http-post.json"), Event.class);
		System.out.println(event.getHttp().getHttpRequestBody());
		System.out.println(event.getHttp().getHttpResponseBody());
	}
	
	@Test
	public void testDNSMetaData() {
		String json = IOUtils.readFileAsString("D:\\Work\\观安信息上海有限公司\\NTA资料\\测试数据\\dga-metadata-response.json");
		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONArray dnsAnswers = jsonObject.getJSONArray("dns_answers");
		Set<Object> ips = dnsAnswers.stream().map((obj) -> {
			JSONObject jObj = (JSONObject) obj;
			String rrtype = jObj.getString("rrtype");
			if ("A".equalsIgnoreCase(rrtype)) {
				return jObj.getString("rdata");
			}
			return null;
		}).filter(Objects::nonNull).collect(toSet());
		
		ips.forEach(System.out::println);
	}
	
}
