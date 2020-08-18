package com.loserico.jackson.listwithtypeinfomixin;

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
		mapper.addMixIn(Animal.class, AnimalMixIn.class);
		mapper.addMixIn(Lion.class, LionMixIn.class);
		mapper.addMixIn(Elephant.class, ElephantMixIn.class);
		mapper.addMixIn(Zoo.class, ZooMixIn.class);
		mapper.writeValue(System.out, zoo);
	}
}
