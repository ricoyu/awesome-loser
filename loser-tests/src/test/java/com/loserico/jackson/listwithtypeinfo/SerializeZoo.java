package com.loserico.jackson.listwithtypeinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2020-08-14 15:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SerializeZoo {
	
	@SneakyThrows
	public static void main(String[] args) {
		Zoo zoo = new Zoo("London Zoo", "London");
		Lion lion = new Lion("Simba");
		Elephant elephant = new Elephant("Manny");
		zoo.addAnimal(elephant).add(lion);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(System.out, zoo);
	}
}
