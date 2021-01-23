package com.loserico.jackson.inherit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.loserico.json.jackson.*;

import java.util.*;

/**
 * <p>
 * Copyright: (C), 2021-01-17 19:34
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InheritDemo {
	
	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		
		Car car = new Car("Mercedes-Benz", "S500", 5, 250.0);
		Truck truck = new Truck("Isuzu", "NQR", 7500.0);
		
		List<Vehicle> vehicles = new ArrayList<>();
		vehicles.add(car);
		vehicles.add(truck);
		
		Fleet serializedFleet = new Fleet();
		serializedFleet.setVehicles(vehicles);
		
		String jsonDataString = mapper.writeValueAsString(serializedFleet);
		System.out.println(jsonDataString);
		
		Fleet deserializedFleet = mapper.readValue(jsonDataString, Fleet.class);
		
		JacksonUtils.toJson(deserializedFleet.getVehicles());
	}
}
