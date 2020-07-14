package com.loserico.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * One final note is that the MyDto class needs to have the no-args default
 * constructor – if it doesn’t, Jackson will not be able to instantiate it:
 * 
 * @author Rico Yu ricoyu520@gmail.com
 * @since 2017-08-04 13:39
 * @version 1.0
 *
 */
public class JacksonArrayListTest {

	/**
	 * Jackson can easily deserialize to a Java Array:
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void givenJsonArray_whenDeserializingAsArray_thenCorrect()
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		List<MyDto> listOfDtos = Lists.newArrayList(
				new MyDto("a", 1, true), new MyDto("bc", 3, false));
		String jsonArray = mapper.writeValueAsString(listOfDtos);

		// [{"stringValue":"a","intValue":1,"booleanValue":true},
		// {"stringValue":"bc","intValue":3,"booleanValue":false}]

		MyDto[] asArray = mapper.readValue(jsonArray, MyDto[].class);
		assertThat(asArray[0], instanceOf(MyDto.class));
	}

	/**
	 * Reading the same JSON Array into a Java Collection is a bit more difficult – by
	 * default, Jackson will not be able to get the full generic type information and
	 * will instead create a collection of LinkedHashMap instances:
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void givenJsonArray_whenDeserializingAsListWithNoTypeInfo_thenNotCorrect()
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();

		List<MyDto> listOfDtos = Lists.newArrayList(
				new MyDto("a", 1, true), new MyDto("bc", 3, false));
		String jsonArray = mapper.writeValueAsString(listOfDtos);

		List<MyDto> asList = mapper.readValue(jsonArray, List.class);
		assertThat((Object) asList.get(0), instanceOf(LinkedHashMap.class));
	}

	/**
	 * There are two ways to help Jackson understand the right type information – we
	 * can either use the TypeReference provided by the library for this very purpose:
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void givenJsonArray_whenDeserializingAsListWithTypeReferenceHelp_thenCorrect()
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();

		List<MyDto> listOfDtos = Lists.newArrayList(
				new MyDto("a", 1, true), new MyDto("bc", 3, false));
		String jsonArray = mapper.writeValueAsString(listOfDtos);

		List<MyDto> asList = mapper.readValue(
				jsonArray, new TypeReference<List<MyDto>>() {
				});
		assertThat(asList.get(0), instanceOf(MyDto.class));
	}

	/**
	 * Or we can use the overloaded readValue method that accepts a JavaType:
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void givenJsonArray_whenDeserializingAsListWithJavaTypeHelp_thenCorrect()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		List<MyDto> listOfDtos = Lists.newArrayList(
				new MyDto("a", 1, true), new MyDto("bc", 3, false));
		String jsonArray = mapper.writeValueAsString(listOfDtos);

		CollectionType javaType = mapper.getTypeFactory()
				.constructCollectionType(List.class, MyDto.class);
		List<MyDto> asList = mapper.readValue(jsonArray, javaType);

		assertThat(asList.get(0), instanceOf(MyDto.class));
	}
}
