package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jackson.deserializer.MongoObjectIdDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2019/12/21 19:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JacksonUtilsTest {
	
	@Test
	public void testSerDeserPlainString() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String str = "aa";
		System.out.println(str + " , length=" + str.length());
		
		String json = objectMapper.writeValueAsString(str);
		String toJson = toJson(str);
		System.out.println(toJson + " , length=" + str.length());
		System.out.println(json + " , length=" + str.length());
		
		/*String simpleJson = IOUtils.readClassPathFileAsString("simple-json.txt");
		System.out.println(JacksonUtils.toObject(simpleJson, String.class));*/
	}
	
	@Test
	public void testDeserialLocalDateTime() {
		String s = "2019-12-21 19:15:34";
		String dateStr = toJson(new DateObj(LocalDateTime.of(2019, 12, 21, 19, 15, 34)));
		System.out.println(dateStr);
		DateObj dateObj = JacksonUtils.toObject(dateStr, DateObj.class);
		Assert.assertEquals(dateObj.getDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), s);
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class DateObj {
		private LocalDateTime datetime;
	}
	
	@Test
	public void testJson2Map() {
		String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
		Map<String, Object> params = JacksonUtils.toMap(jsonString);
		Map<Object, Object> genericMap = JacksonUtils.toGenericMap(jsonString);
	}
	
	@Test
	public void testPojoMap() {
		Map<String, Object> params = new HashMap<>();
		params.put("fullname", "三少爷");
		params.put("birthday", "2020-03-20");
		Person person = JacksonUtils.mapToPojo(params, Person.class);
		System.out.println(person);
		//Assert.assertEquals("三少爷", person.getFullName());
		Map<String, Object> resultMap = JacksonUtils.pojoToMap(person);
		System.out.println(resultMap);
		Assert.assertEquals("2020-03-20", resultMap.get("birthday"));
	}
	
	@Test
	public void testMongoDocumentJson() {
		String json = IOUtils.readClassPathFileAsString("mongodbDocumentJson.json");
		UserInfo userInfo = JacksonUtils.toObject(json, UserInfo.class);
		System.out.println(userInfo.getId());
	}
	
	@Test
	public void testSerializeSingleValue() {
		System.out.println(toJson(1L));
		System.out.println(toJson("hi"));
		System.out.println(toJson(new Object[]{1L, new Date(), null}));
	}
	
	@Data
	public static class UserInfo {
		
		@JsonProperty("_id")
		@JsonDeserialize(using = MongoObjectIdDeserializer.class)
		private String id;
		/**
		 * 用户名
		 */
		private String username;
		/**
		 * 权限，角色
		 */
		private String role;
		/**
		 * 用户密码
		 */
		private String password;
		
		/**
		 * 昵称
		 */
		private String nickname;
		/**
		 * 邮箱
		 */
		private String email;
		
		/**
		 * 锁定状态
		 */
		private boolean lock;
		
		/**
		 * 机构ID
		 */
		@JsonProperty("organ_id")
		private String organId;
		
		/**
		 * 机构ID
		 */
		private Boolean auth;
		/**
		 * 机构
		 */
		@JsonIgnore
		private String organ;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Person {
		
		//private String fullName;
		
		private LocalDate birthday;
	}
	
	@Test
	public void testDeserializeJsonIntTooLong() {
		String json = IOUtils.readClassPathFileAsString("intTooLong.json");
		Event event = JacksonUtils.toObject(json, Event.class);
		System.out.println(toJson(event));
	}
	
	@Data
	private static class Event implements Serializable {
		
		private String timestamp;
		private Long flow_id;
		private String in_iface;
		private String event_type;
		private List<Integer> vlan;
		private String src_ip;
		private Integer src_port;
		private String dest_ip;
		private Integer dest_port;
		private String proto;
		private Metadata metadata;
		private Alert alert;
		private Smtp smtp;
		private String app_proto;
		private String app_proto_ts;
		private Flow flow;
		private String payload;
		private Integer stream;
		private String pcap;
		private Http http;
		private Dns dns;
		private Dga dga;
		
		private String id;
		private Long datetime;
		private String src_country;
		private String src_country_code;
		private String src_longitude;
		private String src_latitude;
		private String dest_country;
		private String dest_country_code;
		private String dest_longitude;
		private String dest_latitude;
		private String probe_ip;
		private String probe_name;
		private String engine;
		private String certainty;
		
		
		@lombok.Data
		public static class Metadata implements Serializable {
			private List<String> flowbits;
		}
		
		@lombok.Data
		public static class Alert implements Serializable {
			private String action;
			private Integer gid;
			private Long signature_id;
			private Integer rev;
			private String signature;
			private String category;
			private Integer severity;
			private String risk_level;
			private String name;
			private String rule_type;
			private String metadata;
		}
		
		@lombok.Data
		public static class Smtp implements Serializable {
			
		}
		
		@lombok.Data
		public static class Flow implements Serializable {
			private Integer pkts_toserver;
			private Integer pkts_toclient;
			private Integer bytes_toserver;
			private Integer bytes_toclient;
			private String start;
		}
		
		@lombok.Data
		public static class Http implements Serializable {
			@lombok.Data
			public static class Header implements Serializable {
				private String name;
				private String value;
				
				public String toString() {
					return String.format("{\"name\":\"%s\",\"value\":\"%s\"}", name, value);
				}
			}
			
			private String hostname;
			private String url;
			private String http_user_agent;
			private String http_content_type;
			private String http_method;
			private String protocol;
			private Integer status;
			private Integer length;
			private List<Header> request_headers;
			private List<Header> response_headers;
			private String httpRequestBody;
			private String httpResponseBody;
		}
		
		
		@lombok.Data
		public static class Dns implements Serializable {
			@lombok.Data
			public static class Answer implements Serializable {
				@lombok.Data
				public static class Authority implements Serializable {
					private String rrname;
					private String rrtype;
					private Integer ttl;
				}
				
				private Integer id;
				private Integer version;
				private String type;
				private String flags;
				private Boolean qr;
				private Boolean aa;
				private Boolean ra;
				private String rrname;
				private String rrtype;
				private String rcode;
				private List<Authority> authorities;
				private String A;
			}
			
			private List<Object> query;
			private Answer answer;
			private Boolean answer_result;
		}
		
		@lombok.Data
		public static class Dga implements Serializable {
			private String domain;
			
			
		}
	}
}
