package com.loserico.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Copyright: (C), 2020-09-28 17:15
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GsonAtomicIntegerSerializeProblem {
	
	public static void main(String[] args) {
		/*GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
		Gson gson = gsonBuilder.create();*/
		//Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		////Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED).create();
		//System.out.println(gson.toJson(new MyClass()));
		//
		//gson = new Gson();
		//System.out.println(gson.toJson(new MyClass()));
		//System.out.println(JacksonUtils.toJson(new MyClass()));
		//System.out.println(JacksonUtils.toJson(new Person("rico")));
		
		GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new FieldExtractor());
		Gson gson = gsonBuilder.create();
		System.out.println(gson.toJson(new MyClass()));
		
		/*Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT)
				.setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						boolean exclude = false;
						try {
							exclude = EXCLUDE.contains(f.getName());
						} catch (Exception ignore) {
						}
						return exclude;
					}
					
					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				})
				.create();
		System.out.println(gson.toJson(new MyClass()));*/
	}
	
	private static final List<String> EXCLUDE = new ArrayList<String>() {{
		add("serialVersionUID");
		add("CREATOR");
	}};
	
	@Data
	@AllArgsConstructor
	static class Person {
		
		private String name;
	}
	
	@Data
	static class MyClass {
		private AtomicInteger variableOne = new AtomicInteger(1);
		private AtomicInteger variableTwo = new AtomicInteger(1);
	}
	
	static class FieldExtractor implements ExclusionStrategy {
		
		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}
		
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			
			if ("serialVersionUID".equals(f.getName())) {
				return true;
			}
			
			return false;
		}
		
	}
}
