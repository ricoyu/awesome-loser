package com.loserico.jackson.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-17 9:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonToListTest {
	
	@SneakyThrows
	@Test
	public void testJson2List() {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
		TypeReference<List<Car>> listTypeReference = new TypeReference<List<Car>>(){};
		List<Car> listCar = objectMapper.readValue(jsonCarArray, listTypeReference);
		assertThat(listCar.get(0).getColor().equals("Black"));
	}
	
	@SneakyThrows
	@Test
	public void testJson2Map() {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
		Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
		});
		System.out.println(map);
	}
	
	/**
	 * UnrecognizedPropertyException
	 */
	@SneakyThrows
	@Test(expected = UnrecognizedPropertyException.class)
	public void testUnknownPropertiesThenThrowUnrecognizedPropertyException() {
		String jsonString = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = objectMapper.readValue(jsonString, Car.class);
	}
	
	@SneakyThrows
	@Test
	public void testUnknownPropertiesThenOk() {
		String jsonString = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Car car = objectMapper.readValue(jsonString, Car.class);
		assertThat(car.getColor().equals("Black"));
	}
	
	@Data
	static class Car {
		private String color;
		
		private String type;
		
	}
}
