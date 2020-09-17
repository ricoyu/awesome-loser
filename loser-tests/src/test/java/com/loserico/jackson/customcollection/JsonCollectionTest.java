package com.loserico.jackson.customcollection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.jackson.customserializer.Car;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonCollectionTest {
	
	@Test
	@SneakyThrows
	public void testJsonArray2JavaArray() {
		String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		Car[] cars = objectMapper.readValue(jsonCarArray, Car[].class);
		assertThat(cars.length).isEqualTo(2);
	}
	
	@SneakyThrows
	@Test
	public void testJsonArray2JavaList() {
		String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
		ObjectMapper objectMapper = new ObjectMapper();
		List<Car> cars = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>() {
		});
		assertThat(cars.size()).isEqualTo(2);
		assertThat(cars.get(0).getType()).isEqualTo("BMW");
	}
}
