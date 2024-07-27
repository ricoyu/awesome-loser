package com.loserico.java14;

import com.loserico.json.jackson.JacksonUtils;

public class TestRecords {

	public static void main(String[] args) {
		var person = new Person(1, "John", 30);
		var person2 = new Person();
		System.out.println(person.name()); // John
		System.out.println(person); // Person[id=1, name=John, age=30]
		System.out.println(person2); // Person[id=0, name=, age=0]

		String json = JacksonUtils.toJson(person);
		System.out.println(json);
		Person person3 = JacksonUtils.toObject(json, Person.class);
		System.out.println(person3); // Person[id=1, name=John, age=30]
	}

}

record Person(Integer id, String name, Integer age) {

	public Person() {
		this(0, "", 0);
	}
}
