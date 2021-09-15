package com.loserico.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-09-02 10:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JsonNodeUtilsTest {
	
	@SneakyThrows
	@Test
	public void test() {
		JsonNode node = JacksonUtils.objectMapper().readTree(IOUtils.readClassPathFileAsBytes("array-field.json"));
		JsonNode jsonNode = node.get("dns_grouped_A");
		if (jsonNode == null) {
			log.info("node not exists");
		}
		
		if (jsonNode.isNull()) {
			log.info("is null");
		}
		
		if (jsonNode.isArray()) {
			List<String> values = new ArrayList<>();
			Iterator<JsonNode> elements = jsonNode.elements();
			log.info("node is array");
			while (elements.hasNext()) {
				JsonNode childNode =  elements.next();
				String nodeValue = childNode.textValue();
				values.add(nodeValue);
			}
			values.forEach(System.out::println);
		}
		
		jsonNode = node.get("dns_answers");
		if (jsonNode.isArray()) {
			Iterator<JsonNode> elements = jsonNode.elements();
			List<DnsAnswer> dnsAnswers = new ArrayList<>();
			while (elements.hasNext()) {
				JsonNode next = elements.next();
				DnsAnswer dnsAnswer = JacksonUtils.toObject(next.toString(), DnsAnswer.class);
				dnsAnswers.forEach(System.out::println);
			}
		}
	}
	
	@Data
	public static class DnsAnswer {
		
		private String rrname;
		
		/**
		 * 解析记录(左边a cname)
		 */
		private String rrtype;
		
		private Long ttl;
		
		/**
		 * 解析记录(右边 10.21.86.106 a.1.com)
		 */
		private String rdata;
	}
}
