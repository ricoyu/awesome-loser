package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.enums.Gender;
import com.loserico.json.jackson.JacksonUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-09-05 10:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EnumDeserializerTest {
	
	@SneakyThrows
	@Test
	public void testDeserializerEnumJsonValue() {
		ObjectMapper mapper = new ObjectMapper();
		Data data = null;
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			data = mapper.readValue("{\"proto_type\": \"tcp(flow)\"}", Data.class);
		}
		long end = System.currentTimeMillis();
		System.out.println("原生ObjectMapper Spent: " + (end - begin));
		assertNotNull(data.getProtoType());
		assertThat(data.getProtoType()).isEqualTo(ProtoType.FLOW_TCP);
		
		JacksonUtils.toJson(data);
		
		begin = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			data = JacksonUtils.toObject("{\"proto_type\": \"tcp(flow)\"}", Data.class);
		}
		end = System.currentTimeMillis();
		System.out.println("JacksonUtils Spent: " + (end - begin));
		assertNotNull(data.getProtoType());
		assertThat(data.getProtoType()).isEqualTo(ProtoType.FLOW_TCP);
		
		String employeeJson = "{ \"name\" : \"Emma\",\"age\":32,\"job\":\"Product Manager\",\"gender\":\"female\",\"salary\":35000 }";
		Employee employee = JacksonUtils.toObject(employeeJson, Employee.class);
		assertThat(employee).isNotNull();
		assertThat(employee.getGender()).isEqualTo(Gender.FEMALE);
	}
	
	
	@lombok.Data
	private static class Data{
		
		@JsonProperty("proto_type")
		private ProtoType protoType;
	}
	
	private static enum ProtoType {
		
		HTTP("http"),
		
		/**
		 * 这个只有网络日志有, 事件是没有的
		 */
		FLOW_TCP("tcp(flow)"),
		
		FLOW_ICMP("icmp(flow)"),
		
		FLOW_UDP("udp(flow)");
		
		@JsonValue
		private String key;
		
		private ProtoType(String key) {
			this.key = key;
		}
	}
	
	@lombok.Data
	private static class Employee {
		private String name;
		
		private Integer age;
		
		private String job;
		
		private Gender gender;
		
		private BigDecimal salary;
	}
}
