package com.loserico.fastjson;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Event implements Serializable {
	
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
