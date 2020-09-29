package com.loserico.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.loserico.json.jackson.JacksonUtils;
import junit.framework.TestCase;
import lombok.Data;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Contains numerous tests involving registered type converters with a Gson instance.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class GsonTypeAdapterTest extends TestCase {
	private Gson gson;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.PROTECTED)
				.registerTypeAdapter(AtomicLong.class, new ExceptionTypeAdapter())
				.registerTypeAdapter(AtomicInteger.class, new AtomicIntegerTypeAdapter())
				.create();
	}
	
	public void testDefaultTypeAdapterThrowsParseException() throws Exception {
		try {
			gson.fromJson("{\"abc\":123}", BigInteger.class);
			fail("Should have thrown a JsonParseException");
		} catch (JsonParseException expected) {
		}
	}
	
	public void testTypeAdapterThrowsException() throws Exception {
		try {
			gson.toJson(new AtomicLong(0));
			fail("Type Adapter should have thrown an exception");
		} catch (IllegalStateException expected) {
		}
		
		try {
			gson.fromJson("123", AtomicLong.class);
			fail("Type Adapter should have thrown an exception");
		} catch (JsonParseException expected) {
		}
	}
	
	@Data
	static class MyClass {
		private AtomicInteger variableOne = null;
		private AtomicInteger variableTwo = new AtomicInteger(1);
	}
	
	public void testTypeAdapterProperlyConvertsTypes() throws Exception {
		MyClass myClass = new MyClass();
		System.out.println(gson.toJson(myClass));
		System.out.println(JacksonUtils.toJson(myClass));
		int intialValue = 1;
		AtomicInteger atomicInt = new AtomicInteger(intialValue);
		String json = gson.toJson(atomicInt);
		System.out.println(json);
		assertEquals(intialValue, Integer.parseInt(json));
		
		atomicInt = gson.fromJson(json, AtomicInteger.class);
		System.out.println(atomicInt);
		assertEquals(intialValue, atomicInt.get());
	}
	
	public void testTypeAdapterDoesNotAffectNonAdaptedTypes() throws Exception {
		String expected = "blah";
		String actual = gson.toJson(expected);
		assertEquals("\"" + expected + "\"", actual);
		
		actual = gson.fromJson(actual, String.class);
		assertEquals(expected, actual);
	}
	
	private static class ExceptionTypeAdapter
			implements JsonSerializer<AtomicLong>, JsonDeserializer {
		@Override
		public JsonElement serialize(
				AtomicLong src, Type typeOfSrc, JsonSerializationContext context) {
			throw new IllegalStateException();
		}
		
		@Override
		public AtomicLong deserialize(
				JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			throw new IllegalStateException();
		}
	}
	
	private static class AtomicIntegerTypeAdapter
			implements JsonSerializer<AtomicInteger>, JsonDeserializer {
		@Override
		public JsonElement serialize(AtomicInteger src, Type typeOfSrc, JsonSerializationContext context) {
			//return new JsonPrimitive(src.incrementAndGet());
			return new JsonPrimitive(src.get());
		}
		
		@Override
		public AtomicInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			int intValue = json.getAsInt();
			return new AtomicInteger(intValue);
		}
	}
	
	static abstract class Abstract {
		String a;
	}
	
	static class Concrete extends Abstract {
		String b;
	}
	
	// https://groups.google.com/d/topic/google-gson/EBmOCa8kJPE/discussion
	public void testDeserializerForAbstractClass() {
		Concrete instance = new Concrete();
		instance.a = "android";
		instance.b = "beep";
		assertSerialized("{\"a\":\"android\"}", Abstract.class, true, true, instance);
		assertSerialized("{\"a\":\"android\"}", Abstract.class, true, false, instance);
		assertSerialized("{\"a\":\"android\"}", Abstract.class, false, true, instance);
		assertSerialized("{\"a\":\"android\"}", Abstract.class, false, false, instance);
		assertSerialized("{\"b\":\"beep\",\"a\":\"android\"}", Concrete.class, true, true, instance);
		assertSerialized("{\"b\":\"beep\",\"a\":\"android\"}", Concrete.class, true, false, instance);
		assertSerialized("{\"b\":\"beep\",\"a\":\"android\"}", Concrete.class, false, true, instance);
		assertSerialized("{\"b\":\"beep\",\"a\":\"android\"}", Concrete.class, false, false, instance);
	}
	
	private void assertSerialized(String expected, Class<?> instanceType, boolean registerAbstractDeserializer,
	                              boolean registerAbstractHierarchyDeserializer, Object instance) {
		JsonDeserializer<Abstract> deserializer = new JsonDeserializer() {
			public Abstract deserialize(JsonElement json, Type typeOfT,
			                            JsonDeserializationContext context) throws JsonParseException {
				throw new AssertionError();
			}
		};
		GsonBuilder builder = new GsonBuilder();
		if (registerAbstractDeserializer) {
			builder.registerTypeAdapter(Abstract.class, deserializer);
		}
		if (registerAbstractHierarchyDeserializer) {
			builder.registerTypeHierarchyAdapter(Abstract.class, deserializer);
		}
		Gson gson = builder.create();
		assertEquals(expected, gson.toJson(instance, instanceType));
	}
}
