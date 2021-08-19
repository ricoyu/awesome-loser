package com.loserico.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.enums.Gender;
import com.loserico.common.lang.i18n.I18N;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jackson.serializer.annotation.EnumI18N;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-08-13 10:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EnumCustomSerializerTest {
	
	@SneakyThrows
	@Test
	public void testEnumCustomSerializer() {
		User user = new User("三少爷");
		Event event = new Event();
		event.setEventName("开会严重超时");
		event.setAttackResult(AttackResult.SUCCESS);
		event.setAttackAgain(AttackResult.SUCCESS);
		event.setPayback(AttackResult.FAIL);
		event.setGender(Gender.FEMALE);
		event.setUser(user);
		event.setBirthday(LocalDateTime.now());
		event.setAttackDirection(AttackDirection.IO);
		
		String json = JacksonUtils.toJson(event);
		System.out.println(json);
	}
	
	@SneakyThrows
	@Test
	public void testEnumSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		User user = new User("三少爷");
		Event event = new Event();
		//event.setEventName("开会严重超时");
		//event.setAttackResult(AttackResult.SUCCESS);
		//event.setAttackAgain(AttackResult.SUCCESS);
		//event.setPayback(AttackResult.FAIL);
		//event.setGender(Gender.FEMALE);
		//event.setUser(user);
		//event.setBirthday(LocalDateTime.now());
		event.setAttackDirection(AttackDirection.IO);
		
		String json = objectMapper.writeValueAsString(event);
		System.out.println(json);
	}
	
	@Data
	public static class Event {
		
		private String eventName;

		@EnumI18N(property = "key", fallbackTo = "desc")
		private AttackResult attackResult;

		@EnumI18N(property = "")
		private AttackResult attackAgain;

		@JsonFormat(shape = JsonFormat.Shape.NUMBER)
		private AttackResult payback;
		
		@JsonProperty("attack_direction")
		private AttackDirection attackDirection;
		
		private Gender gender;

		private LocalDateTime birthday;

		private User user;
	}
	
	@Data
	@AllArgsConstructor
	private class User{
		private String username;
	}
	
	private enum AttackResult {
		
		SUCCESS("result_success", "成功"),
		FAIL("result_fail", "失败");
		
		private String key;
		
		private String desc;
		
		private AttackResult(String key, String desc) {
			this.key = key;
			this.desc = desc;
		}
	}
	
	private enum AttackDirection {
		
		/**
		 * 按照攻击 IP 和目标 IP 的范围（内、外网）来判断过滤符合条件的告警。
		 */
		II("in_in", "横向攻击"),
		
		/**
		 * 击内网为横向攻击，内网攻击外网为外联攻击，外网攻击内网为外部攻击，外网攻击外网为
		 */
		IO("in_out", "外联攻击"),
		
		/**
		 * 来自互联网对内部的攻击。
		 */
		OI("out_in", "外部攻击"),
		
		/**
		 * 城域网互联网出口处, 外网IP攻击外网IP
		 */
		OO("out_out", "互联网攻击"),
		
		OTHER("other", "其他攻击");
		
		@JsonValue
		private String key;
		
		private String desc;
		
		private AttackDirection(String key, String desc) {
			this.key = key;
			this.desc =desc;
		}
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		/**
		 * 处理枚举
		 *
		 * @return
		 */
		public static Map<String, String> handlerEnum() {
			
			Map<String, String> map = new HashMap<>();
			for (AttackDirection attackDirection : AttackDirection.values()) {
				String key = attackDirection.getKey();
				map.put(key, I18N.i18nMessage(key));
			}
			return map;
		}
	}
}
