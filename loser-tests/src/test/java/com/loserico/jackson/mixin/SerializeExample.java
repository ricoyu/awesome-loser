package com.loserico.jackson.mixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.common.lang.utils.FstUtils;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SerializeExample {
	
	@SneakyThrows
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixIn(Bird.class, BirdMixIn.class);
		
		Bird bird = new Bird("scarlet Ibis");
		bird.setSound("eee");
		bird.setHabitat("water");
		
		System.out.println(mapper.writeValueAsString(bird));
		
		Bird bird1 = mapper.readValue("{\"name\":\"scarlet Ibis\",\"sound\":\"eee\",\"habitat\":\"water\"}", Bird.class);
		System.out.println(bird1);
		
		byte[] bytes = FstUtils.toBytes(bird);
		bird1 = FstUtils.toObject(bytes);
		System.out.println(bird1.getHabitat());
	}
}
