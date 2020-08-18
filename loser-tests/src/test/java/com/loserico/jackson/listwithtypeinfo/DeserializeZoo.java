package com.loserico.jackson.listwithtypeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DeserializeZoo {
	
	/**
	 * 会报错, 因为Jackson不知道animals具体的类型应该是什么
	 * @param args
	 */
	@SneakyThrows
	public static void main(String[] args) {
		String json = "{\"name\":\"London Zoo\",\"city\":\"London\",\"animals\":[{\"@class\":\"com.loserico.jackson.listwithtypeinfo.Elephant\",\"name\":\"Manny\"},{\"@class\":\"com.loserico.jackson.listwithtypeinfo.Lion\",\"name\":\"Simba\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		Zoo zoo = mapper.readValue(json, Zoo.class);
		System.out.println(zoo.getName());
	}
}
