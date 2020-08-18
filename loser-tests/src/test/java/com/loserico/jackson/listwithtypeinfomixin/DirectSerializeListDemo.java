package com.loserico.jackson.listwithtypeinfomixin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DirectSerializeListDemo {
	
	@SneakyThrows
	public static void main(String[] args) {
		List animals = new ArrayList();
		Lion lion = new Lion("Samba");
		Elephant elephant = new Elephant("Manny");
		animals.add(lion);
		animals.add(elephant);
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println(mapper.writeValueAsString(animals));
		mapper.writerWithType(new TypeReference<List<Animal>>() {}).writeValue(System.out, animals);
	}
}
