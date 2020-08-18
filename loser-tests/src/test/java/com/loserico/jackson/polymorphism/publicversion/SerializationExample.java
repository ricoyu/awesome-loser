package com.loserico.jackson.polymorphism.publicversion;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-08-14 14:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SerializationExample {
	
	private static String outputFile = "zoo.json";
	
	public static void main(String[] args) throws IOException {
		Zoo zoo = new Zoo("Samba Wild Park", "Paz");
		
		Lion lion = new Lion("Simba");
		Elephant elephant = new Elephant("Manny");
		List<Animal> animals = new ArrayList<>();
		animals.add(lion);
		animals.add(elephant);
		zoo.setAnimals(animals);
		
		ObjectMapper mapper = new ObjectMapper();
		//mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(new File(outputFile)), zoo);
		System.out.println(mapper.writeValueAsString(zoo));
	}
}
