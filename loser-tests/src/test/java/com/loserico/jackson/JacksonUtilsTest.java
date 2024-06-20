package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.loserico.common.lang.i18n.I18N;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jackson.JsonNodeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class JacksonUtilsTest {
	
	@Test
	public void testToList() {
		
		String jsonArr = IOUtils.readClassPathFileAsString("nursery.json");
		List<Nursery> nurseries = JacksonUtils.toList(jsonArr, Nursery.class);
		nurseries.forEach(System.out::println);
	}
	
	static class Nursery {
		private String name;
		
		private String province;
		
		private String city;
		
		private String address;
		
		private String area;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getProvince() {
			return province;
		}
		
		public void setProvince(String province) {
			this.province = province;
		}
		
		public String getCity() {
			return city;
		}
		
		public void setCity(String city) {
			this.city = city;
		}
		
		public String getAddress() {
			return address;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
		
		public String getArea() {
			return area;
		}
		
		public void setArea(String area) {
			this.area = area;
		}
		
	}
	
	@Test
	public void testSerializeNullKey() {
		Map<Object, Object> params = new HashMap<>();
		params.put("one", "hello");
		params.put(1, "asd");
		String output = toJson(params);
		System.out.println(output);
	}
	
	@Test
	public void testEnumDeserializer() {
		String json = "[{\"selectedCentreId\": 1 ,\"closeOperate\": \"0\"}]";
		JacksonUtils.toList(json, AccountCloseVO.class)
				.forEach((accountClose) -> {
					System.out.println(accountClose.getCloseOperate());
				});
		;
	}
	
	public static enum CloseOperate {
		CLOSE,
		OPEN,
	}
	
	public static class AccountCloseVO {
		
		private CloseOperate closeOperate;
		private Long selectedCentreId;
		
		public CloseOperate getCloseOperate() {
			return this.closeOperate;
		}
		
		public void setCloseOperate(CloseOperate closeOperate) {
			this.closeOperate = closeOperate;
		}
		
		public Long getSelectedCentreId() {
			return selectedCentreId;
		}
		
		public void setSelectedCentreId(Long selectedCentreId) {
			this.selectedCentreId = selectedCentreId;
		}
	}
	
	@Test
	public void testArrayToJson() {
		List<Long> centreIds = asList(3L);
		System.out.println(toJson(centreIds));
	}
	
	@Test
	public void testToMap() {
		//String detail = null;
		String detail = "{\"username\":\"admin\",\"password\":\"123456\",\"srcport\":\"11995\",\"pid\":\"43\"}";
		Map<String, Object> map = JacksonUtils.toMap(detail);
		System.out.println(toJson(map));
		assertEquals(toJson(map), detail);
	}
	
	@Test
	public void testToList2() {
		List<String> list = new ArrayList<>();
		String jsonArr = JacksonUtils.toJson(list);
		System.out.println(jsonArr);
		List<String> list2 = JacksonUtils.toList(jsonArr, String.class);
		assertEquals(0, list2.size());
	}
	
	@Test
	public void testSeverityEnum() {
		Event event = new Event(EventSeverity.MEDIUM);
		String json = toJson(event);
		System.out.println(json);
		Event event1 = JacksonUtils.toObject(json, Event.class);
		assertEquals(event1.getSeverity(), event.getSeverity());
	}
	
	@Test
	public void test() {
		String json = "{\"asset\":{\"business_system\":\"NTA流量监测\",\"dapartment\":\"研发部\",\"device_model\":null,\"group\":\"复仇者联盟\",\"ip\":\"112.23.35.36\",\"label\":null,\"logical_address\":null,\"manufacture\":\"联想\",\"name\":\"测试动态添加资产\",\"operating_system\":\"Linux OS\",\"owner\":\"三少爷\",\"physical_address\":\"北京市-北京市\",\"register_date\":\"2021-09-27 09:49:35\",\"remark\":null},\"attack_direction\":\"out_out\",\"attack_result\":\"attempt\",\"attack_stage\":\"攻击尝试\",\"attack_team\":null,\"attacker_ip\":\"59.24.3.174\",\"attacker_mac\":null,\"branch_id\":\"main001\",\"cnnvd_id\":null,\"collect_time\":1633662042950,\"create_time\":1633662025338,\"custom_signature_name\":null,\"cve_id\":null,\"data\":{\"domain\":\"4040139.com\",\"http_accept\":\"application/json, text/plain, */*\",\"http_accept_charset\":null,\"http_accept_encoding\":null,\"http_accept_language\":\"en-US,en;q=0.9,zh;q=0.8,zh-CN;q=0.7,ja;q=0.6,zh-TW;q=0.5,es;q=0.4,es-419;q=0.3,my;q=0.2,nb;q=0.1\",\"http_allow\":\"OPTIONS,GET,POST\",\"http_connection\":\"keep-alive\",\"http_content_length\":22,\"http_cookie\":\"_ga=GA1.2.749682711.1593727565; LF_ID=1632380920044-971095-2504836; Hm_lvt_094d2af1d9a57fd9249b3fa259428445=1632380920; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2217c117c214131b-0b1134465eaebc-a7d173c-2073600-17c117c2142d59%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_landing_page%22%3A%22https%3A%2F%2Fwww.infoq.cn%2F%22%7D%2C%22%24device_id%22%3A%2217c117c214131b-0b1134465eaebc-a7d173c-2073600-17c117c2142d59%22%7D; GRID=6bb9469-01ab26d-b506398-dfdd330; _gid=GA1.2.415450667.1632380920; _gat=1; Hm_lpvt_094d2af1d9a57fd9249b3fa259428445=1632380927; SERVERID=1fa1f330efedec1559b3abbcb6e30f50|1632380927|1632380919\",\"http_method\":\"GET\",\"http_org_src_ip\":\"10.10.26.241\",\"http_origin\":\"https://www.infoq.cn\",\"http_protocol\":\"HTTP/1.1\",\"http_referer\":\"https://www.infoq.cn/\",\"http_true_client_ip\":\"10.10.17.31\",\"http_xff\":\"203.0.113.195, 70.41.3.18, 150.172.238.178\",\"post_data\":\"{\\\"offset\\\":0,\\\"limit\\\":3}\",\"response_authorization\":\"Bearer tCwx9SeqA28WUEJFGTNmhGmHmfOVhZUDKkhijBugZZrTQvqtQTxV0GWc4Xhc1E5LXd\",\"response_body\":\"{\\\"code\\\":0,\\\"data\\\":{},\\\"error\\\":{},\\\"extra\\\":{\\\"cost\\\":0.012025283,\\\"request-id\\\":\\\"8fc10a982e575318b4f7cacc2cd7800a@2@infoq\\\"}}\",\"response_content_type\":\"application/json\",\"response_datetime\":\"Thu, 23 Sep 2021 07:08:47 GMT\",\"status\":200,\"url\":\"/index.html\",\"user_agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36\"},\"dev_id\":\"1\",\"dev_ip\":null,\"dst_city\":\"淮安市\",\"dst_country\":\"中国\",\"dst_ip\":\"112.23.35.36\",\"dst_location_lat\":\"33.879597\",\"dst_location_lon\":\"119.312885\",\"dst_port\":52528,\"dst_province\":\"江苏省\",\"event_count\":31,\"event_date\":\"2021-10-08\",\"event_engine\":\"ids\",\"event_hash\":\"6582021ca82eb1ec6e7357931080b20e70c8fab4efb8786a72bf11fbf2e4a30f\",\"event_id\":\"9b2e4d6a-def3-11eb-af58-000c29238a8d\",\"event_name\":\"Web应用攻击\",\"event_status\":8,\"event_subtype\":\"Web应用攻击\",\"event_subtype_id\":\"B000001\",\"event_type\":\"Web应用攻击\",\"event_type_id\":\"T000001\",\"flow_id\":\"1632278674351\",\"focus_content\":null,\"focus_point\":null,\"focus_status\":0,\"idss_stage_chain\":\"攻击尝试\",\"in_iface\":\"ens192,ens224,ens256\",\"intel\":null,\"is_webattack\":null,\"kill_chain_stage\":\"载荷投递\",\"leader\":true,\"model_ver\":\"6.0.1 RELEASE\",\"modify_time\":1633662025338,\"packet_data\":\"yB9m04LQXOiDeE73CABFIAAoueVAADAGmPQr76fJCgoaFABQzTDr5lHbUCHqH1AQAOVxlQAAAAAAAAAA\",\"packet_file_path\":\"/opt/ids/pcaps/1625642342_232846497_1603924757226902.pcap\",\"packet_size\":null,\"password\":null,\"payload\":\"This is a payload from Rico Yu: 'Hi Avengers!'\",\"pcap\":\"/opt/ids/pcaps/1625642342_232846497_1603924757226902.pcap\",\"proto\":\"TCP\",\"proto_type\":\"http\",\"read_status\":0,\"risk_score\":null,\"rule_id\":2841417,\"security_user_id\":null,\"severity\":\"medium\",\"soar_alert_id\":null,\"soar_linkage_id\":null,\"src_city\":\"浦项市\",\"src_country\":\"韩国\",\"src_ip\":\"59.24.3.174\",\"src_location_lat\":\"36.027428\",\"src_location_lon\":\"129.361570\",\"src_port\":80,\"src_province\":\"庆尚北道\",\"tag\":null,\"ticket_id\":null,\"username\":null,\"victim_ip\":\"112.23.35.36\",\"victim_mac\":null,\"vuln_info\":null,\"vuln_name\":null,\"vuln_type\":null}";
		Event event1 = JacksonUtils.toObject(json, Event.class);
		assertEquals(event1.getSeverity(), EventSeverity.MEDIUM);
	}
	
	@Test
	public void testToJsonNode() {
		String json = "{\"asset\":{\"business_system\":\"NTA流量监测\",\"dapartment\":\"研发部\",\"device_model\":null,\"group\":\"复仇者联盟\",\"ip\":\"112.23.35.36\",\"label\":null,\"logical_address\":null,\"manufacture\":\"联想\",\"name\":\"测试动态添加资产\",\"operating_system\":\"Linux OS\",\"owner\":\"三少爷\",\"physical_address\":\"北京市-北京市\",\"register_date\":\"2021-09-27 09:49:35\",\"remark\":null},\"attack_direction\":\"out_out\",\"attack_result\":\"attempt\",\"attack_stage\":\"攻击尝试\",\"attack_team\":null,\"attacker_ip\":\"59.24.3.174\",\"attacker_mac\":null,\"branch_id\":\"main001\",\"cnnvd_id\":null,\"collect_time\":1633662042950,\"create_time\":1633662025338,\"custom_signature_name\":null,\"cve_id\":null,\"data\":{\"domain\":\"4040139.com\",\"http_accept\":\"application/json, text/plain, */*\",\"http_accept_charset\":null,\"http_accept_encoding\":null,\"http_accept_language\":\"en-US,en;q=0.9,zh;q=0.8,zh-CN;q=0.7,ja;q=0.6,zh-TW;q=0.5,es;q=0.4,es-419;q=0.3,my;q=0.2,nb;q=0.1\",\"http_allow\":\"OPTIONS,GET,POST\",\"http_connection\":\"keep-alive\",\"http_content_length\":22,\"http_cookie\":\"_ga=GA1.2.749682711.1593727565; LF_ID=1632380920044-971095-2504836; Hm_lvt_094d2af1d9a57fd9249b3fa259428445=1632380920; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2217c117c214131b-0b1134465eaebc-a7d173c-2073600-17c117c2142d59%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_landing_page%22%3A%22https%3A%2F%2Fwww.infoq.cn%2F%22%7D%2C%22%24device_id%22%3A%2217c117c214131b-0b1134465eaebc-a7d173c-2073600-17c117c2142d59%22%7D; GRID=6bb9469-01ab26d-b506398-dfdd330; _gid=GA1.2.415450667.1632380920; _gat=1; Hm_lpvt_094d2af1d9a57fd9249b3fa259428445=1632380927; SERVERID=1fa1f330efedec1559b3abbcb6e30f50|1632380927|1632380919\",\"http_method\":\"GET\",\"http_org_src_ip\":\"10.10.26.241\",\"http_origin\":\"https://www.infoq.cn\",\"http_protocol\":\"HTTP/1.1\",\"http_referer\":\"https://www.infoq.cn/\",\"http_true_client_ip\":\"10.10.17.31\",\"http_xff\":\"203.0.113.195, 70.41.3.18, 150.172.238.178\",\"post_data\":\"{\\\"offset\\\":0,\\\"limit\\\":3}\",\"response_authorization\":\"Bearer tCwx9SeqA28WUEJFGTNmhGmHmfOVhZUDKkhijBugZZrTQvqtQTxV0GWc4Xhc1E5LXd\",\"response_body\":\"{\\\"code\\\":0,\\\"data\\\":{},\\\"error\\\":{},\\\"extra\\\":{\\\"cost\\\":0.012025283,\\\"request-id\\\":\\\"8fc10a982e575318b4f7cacc2cd7800a@2@infoq\\\"}}\",\"response_content_type\":\"application/json\",\"response_datetime\":\"Thu, 23 Sep 2021 07:08:47 GMT\",\"status\":200,\"url\":\"/index.html\",\"user_agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36\"},\"dev_id\":\"1\",\"dev_ip\":null,\"dst_city\":\"淮安市\",\"dst_country\":\"中国\",\"dst_ip\":\"112.23.35.36\",\"dst_location_lat\":\"33.879597\",\"dst_location_lon\":\"119.312885\",\"dst_port\":52528,\"dst_province\":\"江苏省\",\"event_count\":31,\"event_date\":\"2021-10-08\",\"event_engine\":\"ids\",\"event_hash\":\"6582021ca82eb1ec6e7357931080b20e70c8fab4efb8786a72bf11fbf2e4a30f\",\"event_id\":\"9b2e4d6a-def3-11eb-af58-000c29238a8d\",\"event_name\":\"Web应用攻击\",\"event_status\":8,\"event_subtype\":\"Web应用攻击\",\"event_subtype_id\":\"B000001\",\"event_type\":\"Web应用攻击\",\"event_type_id\":\"T000001\",\"flow_id\":\"1632278674351\",\"focus_content\":null,\"focus_point\":null,\"focus_status\":0,\"idss_stage_chain\":\"攻击尝试\",\"in_iface\":\"ens192,ens224,ens256\",\"intel\":null,\"is_webattack\":null,\"kill_chain_stage\":\"载荷投递\",\"leader\":true,\"model_ver\":\"6.0.1 RELEASE\",\"modify_time\":1633662025338,\"packet_data\":\"yB9m04LQXOiDeE73CABFIAAoueVAADAGmPQr76fJCgoaFABQzTDr5lHbUCHqH1AQAOVxlQAAAAAAAAAA\",\"packet_file_path\":\"/opt/ids/pcaps/1625642342_232846497_1603924757226902.pcap\",\"packet_size\":null,\"password\":null,\"payload\":\"This is a payload from Rico Yu: 'Hi Avengers!'\",\"pcap\":\"/opt/ids/pcaps/1625642342_232846497_1603924757226902.pcap\",\"proto\":\"TCP\",\"proto_type\":\"http\",\"read_status\":0,\"risk_score\":null,\"rule_id\":2841417,\"security_user_id\":null,\"severity\":\"medium\",\"soar_alert_id\":null,\"soar_linkage_id\":null,\"src_city\":\"浦项市\",\"src_country\":\"韩国\",\"src_ip\":\"59.24.3.174\",\"src_location_lat\":\"36.027428\",\"src_location_lon\":\"129.361570\",\"src_port\":80,\"src_province\":\"庆尚北道\",\"tag\":null,\"ticket_id\":null,\"username\":null,\"victim_ip\":\"112.23.35.36\",\"victim_mac\":null,\"vuln_info\":null,\"vuln_name\":null,\"vuln_type\":null}";
		JsonNode jsonNode = JacksonUtils.readTree(json);
		String severity = JsonNodeUtils.readStr(jsonNode, "severity");
		System.out.println(severity);
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class Event {
		
		private EventSeverity severity;
	}
	
	private static enum EventSeverity {
		
		LOW("low", "低危"),
		
		MEDIUM("medium", "中危"),
		
		HIGH("high", "高危"),
		
		URGENT("urgent", "紧急");
		
		@JsonValue
		private String key;
		
		private String desc;
		
		private EventSeverity(String key, String desc) {
			this.key = key;
			this.desc = desc;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getDesc() {
			return desc;
		}
		
		/**
		 * 处理枚举
		 *
		 * @return
		 */
		public static Map<String, String> handlerEnum() {
			
			Map<String, String> map = new HashMap<>();
			for (EventSeverity eventSeverity : EventSeverity.values()) {
				String key = eventSeverity.getKey();
				map.put(key, I18N.i18nMessage(key));
			}
			return map;
		}
		
		public static String getSpecifiedDesc(String key) {
			for (EventSeverity value : EventSeverity.values()) {
				if (value.getKey().equals(key)) {
					return value.getDesc();
				}
			}
			return null;
		}
	}
}
