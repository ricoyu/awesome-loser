package com.loserico.jackson.javatime.example1;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Demo {

	public static void main(String[] args) throws JsonProcessingException {
		Recording recording = new Recording(1L, "测试序列化成毫秒数", LocalDate.now());
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(recording));
	}
}
